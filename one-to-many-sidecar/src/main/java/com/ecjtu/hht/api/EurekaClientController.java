package com.ecjtu.hht.api;

import com.ecjtu.hht.api.BaseController;
import com.ecjtu.hht.dto.Instance;
import com.ecjtu.hht.dto.InstanceInfoDto;
import com.ecjtu.hht.dto.LeaseInfo;
import com.ecjtu.hht.dto.MyInstanceInfo;
import com.ecjtu.hht.result.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hht
 * @date 2019/11/23 9:20
 */
@RestController
@Slf4j
public class EurekaClientController extends BaseController {
    @Resource
    private EurekaClientConfigBean eurekaClientConfigBean;

    @Value("${server.port}")
    private int port;

    /**
     * eureka 心跳接口
     *
     * @param instanceInfoDto
     * @return
     */
    @PostMapping("/heart/beat")
    public ResponseVo register(@RequestBody InstanceInfoDto instanceInfoDto) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        //构建eureka需要的参数信息
        MyInstanceInfo instanceInfo = buildInstanceInfoDetail(instanceInfoDto);
        //获取eurekaServer地址
        List<String> eurekaServerServiceUrls = eurekaClientConfigBean.getEurekaServerServiceUrls(null);

        //给所有的eurekasServer发送注册请求
        for (String eurekaServerServiceUrl : eurekaServerServiceUrls) {
            log.info("eurekaServer地址为：" + eurekaServerServiceUrl);
            //package  HttpEntity
            HttpEntity<Instance> entity = packageHttpEntity(instanceInfo);
            //发送心跳请求
            boolean success = sendHeartBeatRequest(eurekaServerServiceUrl, instanceInfo, entity, resultMap);
            if (!success) {
                //失败发送注册请求
                success = sendRegisterRequest(eurekaServerServiceUrl, instanceInfo, entity, resultMap);
            }
            if (success) {
                //添加或者更新注册时间
                addOrUpdateInstancesRegisterTime(instanceInfoDto);
            }
        }
        log.info(resultMap.toString());
        return ResponseVo.ok(resultMap);
    }

    /**
     * 添加或者更新注册时间
     *
     * @param instanceInfoDto
     */
    private void addOrUpdateInstancesRegisterTime(@RequestBody InstanceInfoDto instanceInfoDto) {
        //如果已经有了 update否则add
        if (copyOnWriteArrayInstanceSet.contains(instanceInfoDto)) {
            copyOnWriteArrayInstanceSet.forEach(oldInstance -> {
                //更新时间
                if (oldInstance.equals(instanceInfoDto)) {
                    oldInstance.setRegisterTime(System.currentTimeMillis());
                }
            });
        } else {
            //添加注册时间 用于失效剔除
            instanceInfoDto.setRegisterTime(System.currentTimeMillis());
            copyOnWriteArrayInstanceSet.add(instanceInfoDto);
        }
    }

    /**
     * 拉取配置中心配置
     *
     * @return
     */
    @GetMapping("/{application}")
    public String fetchConfig(@PathVariable("application") String application) {
        return super.sendFetchConfigRequest(super.customizeConfigBean.getConfigServerUri(), application);
    }


    /**
     * 包装http请求体
     *
     * @param instanceInfo
     * @return
     */
    private HttpEntity<Instance> packageHttpEntity(MyInstanceInfo instanceInfo) {
        Instance instance = new Instance(instanceInfo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept-Encoding", "gzip");
        headers.add("Accept", "application/json");
        return new HttpEntity<>(instance, headers);
    }

    /**
     * 发送注册请求
     *
     * @param eurekaServerServiceUrl
     * @param instanceInfo
     * @param entity
     * @param resultMap
     * @return http响应状态
     */
    private boolean sendRegisterRequest(String eurekaServerServiceUrl, MyInstanceInfo instanceInfo, HttpEntity<Instance> entity, Map<String, Integer> resultMap) {
        String appNameUpper = instanceInfo.getApp().toUpperCase();
        String registerUrl = eurekaServerServiceUrl + "apps/" + appNameUpper;
        try {
            ResponseEntity<JSONObject> response = super.restTemplate.postForEntity(registerUrl, entity, JSONObject.class);
            //获取http状态 204==success
            HttpStatus statusCode = response.getStatusCode();
            resultMap.put(eurekaServerServiceUrl, statusCode.value());
            return statusCode.value() == 204;
        } catch (Exception ex) {
            log.error("发送注册请求失败", ex);
            resultMap.put(eurekaServerServiceUrl, 500);
            return false;
        }


    }

    /**
     * Url:http://10.10.8.77:7001(eurekaServer)/eureka/apps/CLOUD-SIDECAR(app)/10.10.1.86:cloud-sidecar:8027(instanceId)/status?value=(UP,DOWN)
     * <p>
     * 发送心跳请求
     *
     * @param eurekaServerServiceUrl http://10.10.8.77:7001(eurekaServer)/eureka/
     * @param instanceInfo
     * @param entity
     * @param resultMap
     * @return
     */
    private boolean sendHeartBeatRequest(String eurekaServerServiceUrl, MyInstanceInfo instanceInfo, HttpEntity<Instance> entity, Map<String, Integer> resultMap) {
        String appName = instanceInfo.getApp().toUpperCase();
        String instanceId = instanceInfo.getInstanceId();
        String status = instanceInfo.getStatus();
        String heartBeatUrl = eurekaServerServiceUrl + "apps/" + appName + "/" + instanceId + "/status?value={1}";
        //发送put请求
        try {
            ResponseEntity exchange = super.restTemplate.exchange(heartBeatUrl, HttpMethod.PUT, entity, Object.class, status);
            HttpStatus statusCode = exchange.getStatusCode();
            int httpResponseCode = statusCode.value();
            //200=success
            resultMap.put(eurekaServerServiceUrl, httpResponseCode);
            return httpResponseCode == 200;
        } catch (Exception ex) {
            log.error("发送心跳失败", ex);
            return false;
        }
    }

    /**
     * 拼接eureka需要的参数信息
     *
     * @param instanceInfoDto
     * @return
     */
    private MyInstanceInfo buildInstanceInfoDetail(InstanceInfoDto instanceInfoDto) {
        MyInstanceInfo instanceInfo = new MyInstanceInfo();

        String hostName = instanceInfoDto.getHostName();
        String app = instanceInfoDto.getApp();
        int port = instanceInfoDto.getPort();
        int active = instanceInfoDto.getActive();
        int durationInSecs = instanceInfoDto.getDurationInSecs();
        String instanceId = hostName + ":" + port + "(" + active + ")";
        String ipAddr = instanceInfoDto.getIpAddr();
        String status = instanceInfoDto.getStatus().toUpperCase();
        String localIp = "10.10.1.86";
        String localHostName = "10.10.1.86";
        //构建信息
        instanceInfo.setApp(app);
        instanceInfo.setInstanceId(instanceId);
        instanceInfo.setHostName(localHostName);
        instanceInfo.setIpAddr(localIp);
        instanceInfo.setStatus(status);
        instanceInfo.setOverriddenStatus("UNKNOWN");
        instanceInfo.setPort(new MyInstanceInfo.PortWrapper(this.port, "true"));
        instanceInfo.setSecurePort(new MyInstanceInfo.PortWrapper(443, "false"));
        instanceInfo.setDataCenterInfo(new MyInstanceInfo.DataCenterInfo());
        instanceInfo.setLeaseInfo(new LeaseInfo.Builder().setDurationInSecs(durationInSecs).build());
        String homePageUrl = "http://" + localIp + ":" + this.port + "/";
        instanceInfo.setHomePageUrl(homePageUrl);
        instanceInfo.setStatusPageUrl(homePageUrl + "actuator/info/" + app + "/" + active);
        instanceInfo.setHealthCheckUrl(homePageUrl + "actuator/health/" + app + "/" + active);
        instanceInfo.setVipAddress(app);
        instanceInfo.setSecureVipAddress(app);
        instanceInfo.setIsCoordinatingDiscoveryServer("false");
        instanceInfo.getMetadata().put("management.port", String.valueOf(this.port));
        instanceInfo.getMetadata().put("management.context-path", "/actuator/" + app + "/" + active);
        instanceInfo.getMetadata().put("real.port", String.valueOf(port));
        instanceInfo.getMetadata().put("real.ip", ipAddr);
        return instanceInfo;
    }
}
