package com.ecjtu.hht.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 实例信息dto
 *
 * @author hht
 * @date 2019/11/23 10:05
 */
@Data
public class InstanceInfoDto implements Serializable {
    private String hostName;
    private String app;
    private String ipAddr;
    //UP DOWN
    private String status;
    /**
     * 端口号
     */
    private int port;
    /**
     * eureka失效时间 要小于心跳周期
     */
    private int durationInSecs=15;

    private int active=1;

    private long registerTime;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceInfoDto that = (InstanceInfoDto) o;
        return port == that.port &&
                active == that.active &&
                app.equals(that.app) &&
                ipAddr.equals(that.ipAddr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, ipAddr, port, active);
    }
}
