package com.ecjtu.hht.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author hht
 * @date 2019/12/12 15:46
 */
@Data
@Builder
public class Info {
    private String href;
    private boolean templated;
}
