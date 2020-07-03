package com.ecjtu.hht.api;

import com.ecjtu.hht.config.CustomizeConfigBean;
import com.ecjtu.hht.dto.InstanceInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hht
 * @date 2019/12/2 19:34
 */
@Slf4j
@RestController
public class BaseController {

    @Autowired
    protected CustomizeConfigBean customizeConfigBean;
    @Autowired
    protected RestTemplate restTemplate;

    public static CopyOnWriteArraySet<InstanceInfoDto> copyOnWriteArrayInstanceSet = new CopyOnWriteArraySet<>();

    /**
     * @param configUri
     * @param application
     * @return
     */
    String sendFetchConfigRequest(String configUri, String application) {
        log.info("发送拉取配置请求:" + application);
        String configText = null;
        try {
            String fetchUrl = configUri + application;
            configText = restTemplate.getForObject(fetchUrl, String.class);
        } catch (Exception ex) {
            log.error("发送拉取配置请求失败", ex);
        }
        return configText;
    }

    public static void main(String[] args) {
        int cap=17;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int x = (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        System.out.println(x);
    }
}
