package org.onosporject.pipelines.srv6;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DefaultPortStatistics;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.device.PortStatistics;
import org.onosproject.net.device.PortStatisticsDiscovery;
import org.onosproject.net.driver.AbstractHandlerBehaviour;
import org.onosproject.net.pi.model.PiCounterId;
import org.onosproject.net.pi.model.PiPipeconf;
import org.onosproject.net.pi.runtime.PiCounterCell;
import org.onosproject.net.pi.runtime.PiCounterCellHandle;
import org.onosproject.net.pi.runtime.PiCounterCellId;
import org.onosproject.net.pi.service.PiPipeconfService;
import org.onosproject.p4runtime.api.P4RuntimeClient;
import org.onosproject.p4runtime.api.P4RuntimeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.onosproject.net.pi.model.PiCounterType.INDIRECT;


public class PortStatisticsDiscoveryImpl extends AbstractHandlerBehaviour implements PortStatisticsDiscovery {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final long DEFAULT_P4_DEVICE_ID = 1;

    private static final Map<Pair<DeviceId, PortNumber>, Long> PORT_START_TIMES =
            Maps.newConcurrentMap();

    public PiCounterId ingressCounterId() {
        return Srv6Constants.INGRESS_PORT_COUNTERS_INGRESS_INGRESS_PORT_COUNTER;
    }

    public PiCounterId egressCounterId() {
        return Srv6Constants.EGRESS_PORT_COUNTERS_EGRESS_EGRESS_PORT_COUNTER;
    }

    @Override
    public Collection<PortStatistics> discoverPortStatistics() {

        DeviceService deviceService = this.handler().get(DeviceService.class);
        DeviceId deviceId = this.data().deviceId();

        PiPipeconfService piPipeconfService = handler().get(PiPipeconfService.class);
        if (!piPipeconfService.ofDevice(deviceId).isPresent() ||
                !piPipeconfService.getPipeconf(piPipeconfService.ofDevice(deviceId).get()).isPresent()) {
            log.warn("Unable to get the pipeconf of {}, aborting operation", deviceId);
            return Collections.emptyList();
        }
        PiPipeconf pipeconf = piPipeconfService.getPipeconf(piPipeconfService.ofDevice(deviceId).get()).get();

        P4RuntimeController controller = handler().get(P4RuntimeController.class);
        P4RuntimeClient client = controller.get(deviceId);
        if (client == null) {
            log.warn("Unable to find client for {}, aborting operation", deviceId);
            return Collections.emptyList();
        }

        Map<Long, DefaultPortStatistics.Builder> portStatBuilders = Maps.newHashMap();
        deviceService.getPorts(deviceId)
                .forEach(p -> portStatBuilders.put(
                        p.number().toLong(),
                        DefaultPortStatistics.builder()
                                .setPort(p.number())
                                .setDeviceId(deviceId)
                                .setDurationSec(getDuration(p.number()))));

        Set<PiCounterCellId> counterCellIds = Sets.newHashSet();
        portStatBuilders.keySet().forEach(p -> {
            // Counter cell/index = port number.
            counterCellIds.add(PiCounterCellId.ofIndirect(ingressCounterId(), p));
            counterCellIds.add(PiCounterCellId.ofIndirect(egressCounterId(), p));
        });
        Set<PiCounterCellHandle> counterCellHandles = counterCellIds.stream()
                .map(id -> PiCounterCellHandle.of(deviceId, id))
                .collect(Collectors.toSet());

        // Query the device.
        Collection<PiCounterCell> counterEntryResponse = client.read(
                        DEFAULT_P4_DEVICE_ID, pipeconf)
                .handles(counterCellHandles).submitSync()
                .all(PiCounterCell.class);

        counterEntryResponse.forEach(counterCell -> {
            if (counterCell.cellId().counterType() != INDIRECT) {
                log.warn("Invalid counter data type {}, skipping", counterCell.cellId().counterType());
                return;
            }
            PiCounterCellId indCellId = counterCell.cellId();
            if (!portStatBuilders.containsKey(indCellId.index())) {
                log.warn("Unrecognized counter index {}, skipping", counterCell);
                return;
            }
            DefaultPortStatistics.Builder statsBuilder = portStatBuilders.get(indCellId.index());
            if (counterCell.cellId().counterId().equals(ingressCounterId())) {
                statsBuilder.setPacketsReceived(counterCell.data().packets());
                statsBuilder.setBytesReceived(counterCell.data().bytes());
            } else if (counterCell.cellId().counterId().equals(egressCounterId())) {
                statsBuilder.setPacketsSent(counterCell.data().packets());
                statsBuilder.setBytesSent(counterCell.data().bytes());
            } else {
                log.warn("Unrecognized counter ID {}, skipping", counterCell);
            }

        });

        return portStatBuilders
                .values()
                .stream()
                .map(DefaultPortStatistics.Builder::build)
                .collect(Collectors.toList());
    }

    private long getDuration(PortNumber port) {
        // FIXME: This is a workaround since we cannot determine the port
        // duration from a P4 counter. We'll be fixed by gNMI.
        final long now = System.currentTimeMillis() / 1000;
        final Long startTime = PORT_START_TIMES.putIfAbsent(
                Pair.of(data().deviceId(), port), now);
        return startTime == null ? now : now - startTime;
    }

}
