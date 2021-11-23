#include <core.p4>
#inlcude <v1model.p4>

#define ETH_TYPE_IPV4 0x0800
#define ETH_TYPE_IPV6 0x08dd
#define IP_PROTO_TCP 8w6
#define IP_PROTO_UDP 8w17
#define IP_PROTO_SRV6 8w43
#define IP_VERSION_4 4w4

#define SRV6_MAX_HOPS 6

typedef bit<48> mac_t;
typedef bit<32> ip_address_t;
typedef bit<128> ipv6_address_t;

const port_t CPU_PORT = 255;

// headers

@controller_header("packet_in")
header packet_in_header_t {
    bit<9> ingress_port;
    bit<7> _padding;
}

@controller_header("packet_out")
header packet_out_header_t {
    bit<9> egress_port;
    bit<7> _padding;
}

header ethernet_t {
    bit<48> dst_addr;
    bit<48> src_addr;
    bit<16> ether_type;
}

header ipv4_t {
    bit<4>  version;
    bit<4>  ihl;
    bit<6>  dscp;
    bit<2>  ecn;
    bit<16> len;
    bit<16> identification;
    bit<3>  flags;
    bit<13> frag_offset;
    bit<8>  ttl;
    bit<8>  protocol;
    bit<16> hdr_checksum;
    bit<32> src_addr;
    bit<32> dst_addr;
}

header ipv6_t {
    bit<4> version;
    bit<8> traffic_class;
    bit<20> flow_label;
    bit<16> payload_len;
    bit<8> next_hdr;
    bit<8> hop_limit;
    bit<128> src_addr;
    bit<128> dst_addr;
}

header srv6_header_t {
    bit<8> next_hdr;
    bit<8> hdr_ext_len;
    bit<8> routing_type;
    bit<8> segment_left;
    bit<8> last_entry;
    bit<8> flags;
    bit<16> tag;
}

header srv6_segment_list_t{
    bit<128> sid;
}

header tcp_t {
    bit<16> src_port;
    bit<16> dst_port;
    bit<32> seq_no;
    bit<32> ack_no;
    bit<4>  data_offset;
    bit<3>  res;
    bit<3>  ecn;
    bit<6>  ctrl;
    bit<16> window;
    bit<16> checksum;
    bit<16> urgent_ptr;
}

header udp_t {
    bit<16> src_port;
    bit<16> dst_port;
    bit<16> length_;
    bit<16> checksum;
}

// custom headers

struct headers_t {
    packet_out_header_t packet_out;
    packet_in_header_t packet_in;
    ethernet_t ethernet;
    ipv4_t ipv4;
    ipv6_t ipv6;
    srv6_header_t srh;
    srv6_segment_list_t[SRV6_MAX_HOPS] segment_list;
    tcp_t tcp;
    udp_t udp;
}

struct local_metadata_t {
    bit<16>        l4_src_port;
    bit<16>        l4_dst_port;
    next_hop_id_t  next_hop_id;
    ipv6_address_t next_sid;
    bit<8>         ip_proto;
}
