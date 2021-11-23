package org.onosporject.pipelines.srv6;

import org.onosproject.net.pi.model.*;

/**
 * Constants for srv6 pipeline, which mainly originates from p4info.txt
 */
public final class Srv6Constants {

    private Srv6Constants() {
    }

    // Match Field IDs
    public static final PiMatchFieldId HDR_HDR_IPV4_PROTOCOL =
            PiMatchFieldId.of("hdr.ipv4.protocol");
    public static final PiMatchFieldId HDR_HDR_IPV4_SRC_ADDR =
            PiMatchFieldId.of("hdr.ipv4.src_addr");
    public static final PiMatchFieldId HDR_HDR_ETHERNET_ETHER_TYPE =
            PiMatchFieldId.of("hdr.ethernet.ether_type");
    public static final PiMatchFieldId HDR_HDR_ETHERNET_SRC_ADDR =
            PiMatchFieldId.of("hdr.ethernet.src_addr");
    public static final PiMatchFieldId HDR_LOCAL_METADATA_L4_DST_PORT =
            PiMatchFieldId.of("local_metadata.l4_dst_port");
    public static final PiMatchFieldId HDR_LOCAL_METADATA_L4_SRC_PORT =
            PiMatchFieldId.of("local_metadata.l4_src_port");
    public static final PiMatchFieldId HDR_STANDARD_METADATA_INGRESS_PORT =
            PiMatchFieldId.of("standard_metadata.ingress_port");
    public static final PiMatchFieldId HDR_HDR_IPV4_DST_ADDR =
            PiMatchFieldId.of("hdr.ipv4.dst_addr");
    public static final PiMatchFieldId HDR_LOCAL_METADATA_NEXT_HOP_ID =
            PiMatchFieldId.of("local_metadata.next_hop_id");
    public static final PiMatchFieldId HDR_HDR_ETHERNET_DST_ADDR =
            PiMatchFieldId.of("hdr.ethernet.dst_addr");
    public static final PiMatchFieldId HDR_HDR_IPV6_DST_ADDR =
            PiMatchFieldId.of("hdr.ipv6.dst_addr");

    // Table IDs
    public static final PiTableId INGRESS_HOST_METER_CONTROL_HOST_METER_TABLE =
            PiTableId.of("ingress.host_meter_control.host_meter_table");
    public static final PiTableId INGRESS_TABLE0_CONTROL_TABLE0 =
            PiTableId.of("ingress.table0_control.table0");
    public static final PiTableId INGRESS_TABLES_CONTROL_LOCAL_SID_TABLE =
            PiTableId.of("ingress.tables_control.local_sid_table");
    public static final PiTableId INGRESS_TABLES_CONTROL_TRANSIT_TABLE =
            PiTableId.of("ingress.tables_control.transit_table");
    public static final PiTableId INGRESS_TABLES_CONTROL_ROUTING_V6_TABLE =
            PiTableId.of("ingress.tables_control.routing_v6_table");

    // Action IDs
    public static final PiActionId NO_ACTION = PiActionId.of("NoAction");

    public static final PiActionId INGRESS_TABLE0_CONTROL_SEND_TO_CPU =
            PiActionId.of("ingress.table0_control.send_to_cpu");
    public static final PiActionId INGRESS_TABLE0_CONTROL_SET_NEXT_HOP_ID =
            PiActionId.of("ingress.table0_control.set_next_hop_id");
    public static final PiActionId INGRESS_TABLE0_CONTROL_SET_EGRESS_PORT =
            PiActionId.of("ingress.table0_control.set_egress_port");
    public static final PiActionId INGRESS_TABLE0_CONTROL_DROP =
            PiActionId.of("ingress.table0_control.drop");
    public static final PiActionId INGRESS_HOST_METER_CONTROL_READ_METER =
            PiActionId.of("ingress.host_meter_control.read_meter");

    public static final PiActionId INGRESS_TABLES_CONTROL_DROP =
            PiActionId.of("ingress.tables_control.drop");
    public static final PiActionId INGRESS_TABLES_CONTROL_END =
            PiActionId.of("ingress.tables_control.end");
    public static final PiActionId INGRESS_TABLES_CONTROL_INSERT_SEGMENT_LIST_2 =
            PiActionId.of("ingress.tables_control.insert_segment_list_2");
    public static final PiActionId INGRESS_TABLES_CONTROL_INSERT_SEGMENT_LIST_3 =
            PiActionId.of("ingress.tables_control.insert_segment_list_3");
    public static final PiActionId INGRESS_TABLES_CONTROL_INSERT_SEGMENT_LIST_4 =
            PiActionId.of("ingress.tables_control.insert_segment_list_4");
    public static final PiActionId INGRESS_TABLES_CONTROL_INSERT_SEGMENT_LIST_5 =
            PiActionId.of("ingress.tables_control.insert_segment_list_5");
    public static final PiActionId INGRESS_TABLES_CONTROL_POP =
            PiActionId.of("ingress.tables_control.pop");
    public static final PiActionId INGRESS_TABLES_CONTROL_TO_PORT =
            PiActionId.of("ingress.tables_control.to_port");

    // Action Param IDs
    public static final PiActionParamId PORT = PiActionParamId.of("port");
    public static final PiActionParamId NEXT_HOP_ID =
            PiActionParamId.of("next_hop_id");

    // Indirect Counter IDs
    public static final PiCounterId EGRESS_PORT_COUNTERS_EGRESS_EGRESS_PORT_COUNTER =
            PiCounterId.of("egress.port_counters_egress.egress_port_counter");

    public static final PiCounterId INGRESS_PORT_COUNTERS_INGRESS_INGRESS_PORT_COUNTER =
            PiCounterId.of("ingress.port_counters_ingress.ingress_port_counter");

    // Direct Counter IDs
    public static final PiCounterId INGRESS_TABLES_CONTROL_LOCAL_SID_TABLE_COUNTER =
            PiCounterId.of("ingress.tables_control.local_sid_table_counter");

    public static final PiCounterId INGRESS_TABLES_CONTROL_TRANSIT_TABLE_COUNTER =
            PiCounterId.of("ingress.tables_control.transit_table_counter");

    public static final PiCounterId INGRESS_TABLES_CONTROL_ROUTING_V6_TABLE_COUNTER =
            PiCounterId.of("ingress.tables_control.routing_v6_table_counter");

    // Meter IDs
    public static final PiMeterId INGRESS_PORT_METERS_INGRESS_INGRESS_PORT_METER =
            PiMeterId.of("ingress.port_meters_ingress.ingress_port_meter");

    public static final PiMeterId EGRESS_PORT_METERS_EGRESS_EGRESS_PORT_METER =
            PiMeterId.of("egress.port_meters_egress.egress_port_meter");

    // Packet Metadata IDs
    public static final PiPacketMetadataId INGRESS_PORT =
            PiPacketMetadataId.of("ingress_port");
    public static final PiPacketMetadataId EGRESS_PORT =
            PiPacketMetadataId.of("egress_port");
}
