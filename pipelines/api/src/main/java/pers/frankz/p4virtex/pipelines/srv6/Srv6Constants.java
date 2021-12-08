package pers.frankz.p4virtex.pipelines.srv6;

import org.onosproject.net.pi.model.*;

/**
 * SRv6 pipeline 涉及的常量，主要来源于p4info中定义的table，action以及相应的param
 */
public class Srv6Constants {

    private Srv6Constants() {
    }

    // Match Field IDs
    public static final PiMatchFieldId STANDARD_METADATA_INGRESS_PORT =
            PiMatchFieldId.of("standard_metadata.ingress_port");

    public static final PiMatchFieldId HDR_ETHERNET_SRC_ADDR =
            PiMatchFieldId.of("hdr.ethernet.src_addr");

    public static final PiMatchFieldId HDR_ETHERNET_DST_ADDR =
            PiMatchFieldId.of("hdr.ethernet.dst_addr");

    public static final PiMatchFieldId HDR_ETHERNET_ETHER_TYPE =
            PiMatchFieldId.of("hdr.ethernet.ether_type");

    public static final PiMatchFieldId LOCAL_METADATA_IP_PROTO =
            PiMatchFieldId.of("local_metadata.ip_proto");

    public static final PiMatchFieldId LOCAL_METADATA_ICMP_TYPE =
            PiMatchFieldId.of("local_metadata.icmp_type");

    public static final PiMatchFieldId LOCAL_METADATA_ICMPV6_TYPE =
            PiMatchFieldId.of("local_metadata.icmpv6_type");

    public static final PiMatchFieldId HDR_IPV6_DST_ADDR =
            PiMatchFieldId.of("hdr.ipv6.dst_addr");

    // Table IDs
    public static final PiTableId INGRESS_LOCAL_MAC_TABLE =
            PiTableId.of("ingress.local_mac_table");

    public static final PiTableId INGRESS_ROUTING_V6_TABLE =
            PiTableId.of("ingress.routing_v6_table");

    public static final PiTableId INGRESS_LOCAL_SID_TABLE =
            PiTableId.of("ingress.local_sid_table");

    public static final PiTableId INGRESS_TRANSIT_TABLE =
            PiTableId.of("ingress.transit_table");

    public static final PiTableId INGRESS_COMMON_TABLE =
            PiTableId.of("ingress.common_table");

    // Action IDs
    public static final PiActionId NO_ACTION = PiActionId.of("NoAction");

    public static final PiActionId INGRESS_SET_NEXT_HOP =
            PiActionId.of("ingress.set_next_hop");

    public static final PiActionId INGRESS_END =
            PiActionId.of("ingress.end");

    public static final PiActionId INGRESS_INSERT_SEGMENT_LIST_2 =
            PiActionId.of("ingress.insert_segment_list_2");

    public static final PiActionId INGRESS_INSERT_SEGMENT_LIST_3 =
            PiActionId.of("ingress.insert_segment_list_3");

    public static final PiActionId INGRESS_INSERT_SEGMENT_LIST_4 =
            PiActionId.of("ingress.insert_segment_list_4");

    public static final PiActionId INGRESS_SRV6_POP =
            PiActionId.of("ingress.srv6_pop");

    public static final PiActionId INGRESS_DROP =
            PiActionId.of("ingress.drop");

    public static final PiActionId INGRESS_SEND_TO_CPU =
            PiActionId.of("ingress.send_to_cpu");

    public static final PiActionId INGRESS_SET_EGRESS_PORT =
            PiActionId.of("ingress.set_egress_port");

    // Action Param IDs
    public static final PiActionParamId PORT = PiActionParamId.of("port");

    public static final PiActionParamId DST_MAC = PiActionParamId.of("dmac");

    public static final PiActionParamId S1 = PiActionParamId.of("s1");

    public static final PiActionParamId S2 = PiActionParamId.of("s2");

    public static final PiActionParamId S3 = PiActionParamId.of("s3");

    public static final PiActionParamId S4 = PiActionParamId.of("s4");

    // Packet Metadata IDs
    public static final PiPacketMetadataId INGRESS_PORT =
            PiPacketMetadataId.of("ingress_port");
    public static final PiPacketMetadataId EGRESS_PORT =
            PiPacketMetadataId.of("egress_port");
}
