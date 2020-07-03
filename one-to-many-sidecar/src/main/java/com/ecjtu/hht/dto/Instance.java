package com.ecjtu.hht.dto;

import lombok.Data;

/**
 * @author hht
 * @date 2019/11/23 18:28
 */
@Data
public class Instance {
    private MyInstanceInfo instance;

    public Instance(MyInstanceInfo instance) {
        this.instance = instance;
    }
}
