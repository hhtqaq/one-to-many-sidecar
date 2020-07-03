package com.ecjtu.hht.api;

import com.ecjtu.hht.api.BaseController;
import com.ecjtu.hht.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hht
 * @date 2019/12/5 17:58
 */
@RestController
public class ActuatorController {

    @Value("${server.port}")
    private int port;
    @Value("${server.ip}")
    private String ip;


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path = {"/actuator/{app}/{active}"})
    public ActuatorInfo getActuatorInfo(@PathVariable("app") String app, @PathVariable("active") int active) {
        String href = "http://" + ip + ":" + port + "/actuator/info/" + app + "/" + active;
        Links links = Links.builder().info(Info.builder().href(href).templated(false).build()).build();
        return ActuatorInfo.builder()
                .links(links)
                .build();
    }

    /**
     * 转发info信息请求
     *
     * @param app
     * @param active
     * @return
     */
    @RequestMapping(path = "/actuator/info/{app}/{active}")
    public InfoDto getVersion(@PathVariable("app") String app, @PathVariable("active") int active) {
        //过滤找到的实例
        String infoUrl = getInstanceIp(app, active) + "/actuator/info";
        // 调用目标实例接口获取版本信息
        return restTemplate.getForEntity(infoUrl, InfoDto.class).getBody();
    }


    /**
     * 健康状态  防止404
     *
     * @param app
     * @return
     */
    @RequestMapping("/actuator/health/{app}/{active}")
    public Object getHealth(@PathVariable("app") String app, @PathVariable("active") int active) {
        //获取c++程序的健康状态：
        String healthUrl = getInstanceIp(app, active) + "/actuator/health";
        return restTemplate.getForEntity(healthUrl, Object.class);
    }

    /**
     * 根据app 和active获取指定实例
     *
     * @param app    应用名
     * @param active 实例号
     * @return
     */
    private String getInstanceIp(String app, int active) {
        InstanceInfoDto targetInstance = BaseController.copyOnWriteArrayInstanceSet
                .stream()
                .filter(instanceInfoDto -> instanceInfoDto.getApp().equals(app) && instanceInfoDto.getActive() == active)
                .findFirst()
                .orElse(new InstanceInfoDto());
        return "http://" + targetInstance.getIpAddr() + ":" + targetInstance.getPort();
    }
}
