package com.ecjtu.hht.task;

import com.ecjtu.hht.api.BaseController;
import com.ecjtu.hht.dto.InstanceInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 失效剔除任务
 *
 * @author hht
 * @date 2019/12/3 15:39
 */
@Component
@Slf4j
public class ExpirationTask {

    private static final int leaseExpirationDurationInSeconds = 60;

    @Scheduled(fixedRate = 1000 * leaseExpirationDurationInSeconds)
    public void run() {
        log.info("失效剔除任务开始");
        CopyOnWriteArraySet<InstanceInfoDto> copyOnWriteArraySet = BaseController.copyOnWriteArrayInstanceSet;
        for (InstanceInfoDto instanceInfoDto : copyOnWriteArraySet) {
            //注册时间
            long registerTime = instanceInfoDto.getRegisterTime();
            //c++设置的失效时间  增加一个5s的偏移量  避免时间不太精确
            int durationInMillis = (instanceInfoDto.getDurationInSecs()) * 1000;
            //当前时间
            long currentTime = System.currentTimeMillis();
            if ((currentTime - registerTime) > durationInMillis) {
                log.info("剔除{}", instanceInfoDto.getApp() + instanceInfoDto.getActive());
                copyOnWriteArraySet.remove(instanceInfoDto);
            }
        }
    }
}
