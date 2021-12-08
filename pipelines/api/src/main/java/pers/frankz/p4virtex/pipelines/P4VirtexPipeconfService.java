package pers.frankz.p4virtex.pipelines;

import org.onosproject.net.pi.model.DefaultPiPipeconf;
import org.onosproject.net.pi.model.PiPipeconf;

import java.net.URL;

public interface P4VirtexPipeconfService {

    /**
     * 构建个pipeconf, 暂时只支持BMV2-target
     *
     * @param pipeConfId pipeConf id
     * @param p4InfoUrl p4_info url
     * @param bmv2JsonUrl bmv2_json url
     * @return
     */
    PiPipeconf buildPipeconf(String pipeConfId, URL p4InfoUrl, URL bmv2JsonUrl);
}
