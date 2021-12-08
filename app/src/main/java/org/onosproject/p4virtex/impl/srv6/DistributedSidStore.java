package org.onosproject.p4virtex.impl.srv6;

import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.srv6.Sid;
import org.onosproject.p4virtex.srv6.SidStore;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.checkState;

@Component(immediate = true, service = SidStore.class)
public class DistributedSidStore implements SidStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<DeviceId, Set<Sid>> sidPool = new HashMap<>();

    @Activate
    public void activate() {
        log.info("sid store started...");
    }

    @Deactivate
    public void deactivate() {
        log.info("sid store stopped..");
    }


    @Override
    public void addSid(DeviceId deviceId, Sid sid) {
        Set<Sid> sidSet = sidPool.get(deviceId);
        if (sidSet == null) {
            sidSet = new HashSet<>();
        }
        checkState(!sidSet.contains(sid), "The sid has been existed...");
        sidSet.add(sid);
    }

    @Override
    public Set<Sid> getSidListByDeviceId(DeviceId deviceId) {
        return sidPool.get(deviceId);
    }

    @Override
    public Set<Sid> getAllSidsByNetworkId(NetworkId networkId) {
        return null;
    }
}
