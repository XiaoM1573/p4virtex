

#ifndef __TABLES__
#define __TABLES__

#include "defines.p4"
#include "headers.p4"

control tables_control(inout headers_t hdr,
                          inout local_metadata_t local_metadata,
                          inout standard_metadata_t standard_metadata) {


    action send_to_cpu(){
        standard_metadata.egress_spec = CPU_PORT;
    }

    action set_egress_port(port_t port){
        standard_metadata.egress_spec = port;
    }

    action drop(){
        mark_to_drop(standard_metadata);
    }


    // End
    // Decrement the "segment left" field from the srv6 header
    // Set destination IP address to next segment
    action end(){
        hdr.srh.segment_left = hdr.srh.segment_left - 1;
        hdr.ipv6.dst_addr = local_metadata.next_sid;
    }

    // End.X
    action end_x(port_t port){
        end();
        set_egress_port(port);
    }

    direct_counter(CounterType.packets_and_bytes) local_sid_table_counter;

    table local_sid_table{
        key = {
            hdr.ipv6.dst_addr: lpm;
        }
        actions = {
            end;
        }
        const default_action = end();
        counters = local_sid_table_counter;
    }

    action insert_srh(bit<4> num_segments){
        hdr.srh.setValid();
        hdr.srh.next_hdr = hdr.ipv6.next_hdr;
        hdr.srh.hdr_ext_len = num_segments * 2;
        hdr.srh.routing_type = 4;
        hdr.srh.segment_left = num_segments - 1;
        hdr.srh.last_entry = num_segments - 1;
        hdr.srh.flags = 0;
        hdr.srh.tag = 0;
        hdr.ipv6.next_hdr = IP_PROTO_SRV6;
    }

    // last s* represents dst addr
    action insert_segment_list_2(ipv6_addr_t s1, ipv6_addr_t s2){
        hdr.ipv6.dst_addr = s1;
        hdr.ipv6.payload_len = hdr.ipv6.payload_len + 40;
        insert_srh(2);
        hdr.segment_list[0].setValid();
        hdr.segment_list[0].sid = s2;
        hdr.segment_list[1].setValid();
        hdr.segment_list[1].sid = s1;
    }

    action insert_segment_list_3(ipv6_addr_t s1, ipv6_addr_t s2, ipv6_addr_t s3){
        hdr.ipv6.dst_addr = s1;
        hdr.ipv6.payload_len = hdr.ipv6.payload_len + 56;
        insert_srh(3);
        hdr.segment_list[0].setValid();
        hdr.segment_list[0].sid = s3;
        hdr.segment_list[1].setValid();
        hdr.segment_list[1].sid = s2;
        hdr.segment_list[2].setValid();
        hdr.segment_list[2].sid = s1;
    }

    action insert_segment_list_4(ipv6_addr_t s1, ipv6_addr_t s2, ipv6_addr_t s3, ipv6_addr_t s4){
        hdr.ipv6.dst_addr = s1;
        hdr.ipv6.payload_len = hdr.ipv6.payload_len + 72;
        insert_srh(4);
        hdr.segment_list[0].setValid();
        hdr.segment_list[0].sid = s4;
        hdr.segment_list[1].setValid();
        hdr.segment_list[1].sid = s3;
        hdr.segment_list[2].setValid();
        hdr.segment_list[2].sid = s2;
        hdr.segment_list[3].setValid();
        hdr.segment_list[3].sid = s1;
    }

    action insert_segment_list_5(ipv6_addr_t s1, ipv6_addr_t s2, ipv6_addr_t s3, ipv6_addr_t s4, ipv6_addr_t s5){
        hdr.ipv6.dst_addr = s1;
        hdr.ipv6.payload_len = hdr.ipv6.payload_len + 88;
        insert_srh(5);
        hdr.segment_list[0].setValid();
        hdr.segment_list[0].sid = s5;
        hdr.segment_list[1].setValid();
        hdr.segment_list[1].sid = s4;
        hdr.segment_list[2].setValid();
        hdr.segment_list[2].sid = s3;
        hdr.segment_list[3].setValid();
        hdr.segment_list[3].sid = s2;
        hdr.segment_list[4].setValid();
        hdr.segment_list[4].sid = s1;
    }

    direct_counter(CounterType.packets_and_bytes) transit_table_counter;

    table transit_table{
        key = {
            hdr.ipv6.dst_addr: lpm;
        }

        actions = {
            insert_segment_list_2;
            insert_segment_list_3;
            insert_segment_list_4;
            insert_segment_list_5;
        }

        counters = transit_table_counter;
    }

    // Flavor: PSP
    action pop(){
        hdr.ipv6.next_hdr = hdr.srh.next_hdr;
        bit<16> srh_size = (((bit<16>)hdr.srh.last_entry + 1) << 4) + 8;
        hdr.ipv6.payload_len = hdr.ipv6.payload_len - srh_size;

        hdr.srh.setInvalid();
        hdr.segment_list[0].setInvalid();
        hdr.segment_list[1].setInvalid();
        hdr.segment_list[2].setInvalid();
        hdr.segment_list[3].setInvalid();
        hdr.segment_list[4].setInvalid();
        hdr.segment_list[5].setInvalid();
    }

    action set_next_hop(mac_t dmac){
        hdr.ethernet.src_addr = hdr.ethernet.dst_addr;
        hdr.ethernet.dst_addr = dmac;
        // Decrement TTL
        hdr.ipv6.hop_limit = hdr.ipv6.hop_limit - 1;
    }

    action to_port(port_t port){
        set_egress_port(port);
        hdr.ipv6.hop_limit = hdr.ipv6.hop_limit - 1;
    }


    direct_counter(CounterType.packets_and_bytes) routing_v6_table_counter;

    table routing_v6_table{
        key = {
            hdr.ipv6.dst_addr: lpm;
        }
        actions = {
            set_next_hop;
        }

        counters = routing_v6_table_counter;
    }


    apply{
        if(hdr.ipv6.isValid()){
            if(local_sid_table.apply().hit){
                if(hdr.srh.isValid() && hdr.srh.segment_left == 0){
                    pop();
                }
            }else{
                transit_table.apply();
            }
            routing_v6_table.apply();
            if(hdr.ipv6.hop_limit == 0 ){
                drop();
            }
        }
    }
}

#endif