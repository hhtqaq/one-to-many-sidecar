package com.ecjtu.hht.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class MyInstanceInfo implements Serializable {

    private String instanceId;
    private String hostName;
    private String app;
    private String ipAddr;
    private String status;
    private String overriddenStatus;
    private PortWrapper port;

    private PortWrapper securePort;
    private int countryId=1;
    private DataCenterInfo dataCenterInfo;
    private LeaseInfo leaseInfo;
    private Map<String, String> metadata;

    private Long lastUpdatedTimestamp;
    private Long lastDirtyTimestamp;

    private String homePageUrl;
    private String statusPageUrl;
    private String healthCheckUrl;
    private String vipAddress;
    private String secureVipAddress;
    private String isCoordinatingDiscoveryServer;
    public MyInstanceInfo() {
        this.metadata = new ConcurrentHashMap<String, String>();
        this.lastUpdatedTimestamp = System.currentTimeMillis();
        this.lastDirtyTimestamp = lastUpdatedTimestamp;
        this.dataCenterInfo=new DataCenterInfo();
    }

    public static class PortWrapper implements Serializable {
        @JsonProperty("$")
        private int port;

        @JsonProperty("@enabled")
        private String enabled;

        public PortWrapper(int port,String enabled){
            this.enabled=enabled;
            this.port=port;
        }
    }
    public static class DataCenterInfo implements Serializable {
        @JsonProperty("@class")
        private String clazz="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo";
        @JsonProperty("name")
        private String name="MyOwn";
    }


}
