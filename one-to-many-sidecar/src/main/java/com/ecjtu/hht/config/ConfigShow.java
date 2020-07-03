/*
package com.tvt.hht.my_sidecar.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

*/
/**
 * @Auther: TX
 * @Description: 配置打印
 *//*

@Component
@Slf4j
@RefreshScope
public class ConfigShow {
    private static final String MOD_CONFIGSHOW = "ConfigShow";

    private String c1;
    @Value("${eureka.instance.instance-id}")
    public void setC1(String c1) {
        this.c1 = c1;
        log.info(String.format("[配置参数] eureka.instance.instance-id=%s", c1), MOD_CONFIGSHOW);
    }

    private String c2;
    @Value("${eureka.client.serviceUrl.defaultZone}")
    public void setC2(String c2) {
        this.c2 = c2;
        log.info(String.format("[配置参数] eureka.client.serviceUrl.defaultZone=%s", c2), MOD_CONFIGSHOW);
    }

    private String c3;
    @Value("${spring.cloud.stream.kafka.binder.zkNodes}")
    public void setC3(String c3) {
        this.c3 = c3;
        log.info(String.format("[配置参数] spring.cloud.stream.kafka.binder.zkNodes=%s", c3), MOD_CONFIGSHOW);
    }

    private String c4;
    @Value("${spring.cloud.stream.kafka.binder.brokers}")
    public void setC4(String c4) {
        this.c4 = c4;
        log.info(String.format("[配置参数] spring.cloud.stream.kafka.binder.brokers=%s", c4), MOD_CONFIGSHOW);
    }

    private String c5;
    @Value("${spring.cloud.stream.bindings.springCloudBusInput.destination}")
    public void setC5(String c5) {
        this.c5 = c5;
        log.info(String.format("[配置参数] spring.cloud.stream.bindings.springCloudBusInput.destination=%s", c5), MOD_CONFIGSHOW);
    }

    private String c6;
    @Value("${spring.cloud.stream.bindings.springCloudBusOutput.destination}")
    public void setC6(String c6) {
        this.c6 = c6;
        log.info(String.format("[配置参数] spring.cloud.stream.bindings.springCloudBusOutput.destination=%s", c6), MOD_CONFIGSHOW);
    }

    private String c7;
    @Value("${sidecar.port}")
    public void setC7(String c7) {
        this.c7 = c7;
        log.info(String.format("[配置参数] sidecar.port=%s", c7), MOD_CONFIGSHOW);
    }


}
*/
