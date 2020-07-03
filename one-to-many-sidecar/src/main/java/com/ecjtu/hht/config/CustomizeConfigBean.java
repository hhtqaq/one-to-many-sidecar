package com.ecjtu.hht.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hht
 * @date 2019/12/3 14:52
 */
@Data
@Component
@ConfigurationProperties(prefix = CustomizeConfigBean.CUSTOMIZE_SIDECAR_CONFIG_PREFIX)
public class CustomizeConfigBean {

    public static final String CUSTOMIZE_SIDECAR_CONFIG_PREFIX = "customize.sidecar";
    /**
     * 配置中心uri
     */
    private String configServerUri;
    /**
     * 接收配置url
     */
    private String receiveConfigUrl;

}
