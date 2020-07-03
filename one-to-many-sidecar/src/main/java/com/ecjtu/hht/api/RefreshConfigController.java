package com.ecjtu.hht.api;

import com.ecjtu.hht.api.BaseController;
import com.ecjtu.hht.dto.InstanceInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 刷新配置
 *
 * @author hht
 * @date 2019/12/2 19:17
 */
@Slf4j
@RestController
public class RefreshConfigController extends BaseController {

    public static final String CONFIG_SUFFIX = ".properties";

    public static final String HTTP_PREFIX = "http://";


    @PostMapping("/refresh")
    public void refresh(@RequestParam(required = false, value = "destination") String destination) {
        if (CollectionUtils.isEmpty(copyOnWriteArrayInstanceSet)) {
            return;
        }
        //如果指定了刷新某个配置文件 那么就只给他发 destination=app
        if (!StringUtils.isEmpty(destination)) {
            copyOnWriteArrayInstanceSet = copyOnWriteArrayInstanceSet.stream().filter(instanceInfoDto -> instanceInfoDto.getApp().equals(destination)).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
        }
        copyOnWriteArrayInstanceSet.forEach(instanceInfoDto -> {
            String application = instanceInfoDto.getApp() + "-" + instanceInfoDto.getActive() + CONFIG_SUFFIX;
            //发送拉取配置
            String configText = super.sendFetchConfigRequest(customizeConfigBean.getConfigServerUri(), application);
            //推送配置
            sendPushConfigRequest(instanceInfoDto, configText);
        });
    }

    /**
     * 发送推送配置请求
     *
     * @param instanceInfoDto
     * @param configText      配置内容
     */
    private void sendPushConfigRequest(InstanceInfoDto instanceInfoDto, String configText) {
        String receiveConfigUrl = super.customizeConfigBean.getReceiveConfigUrl();
        String pushUri = HTTP_PREFIX + instanceInfoDto.getIpAddr() + ":" + instanceInfoDto.getPort() + receiveConfigUrl;
        log.info("推送配置" + pushUri);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(configText, headers);
            restTemplate.postForEntity(pushUri, request, String.class);
        } catch (Exception ex) {
            log.error("推送配置失败" + pushUri, ex);
        }
    }
}
