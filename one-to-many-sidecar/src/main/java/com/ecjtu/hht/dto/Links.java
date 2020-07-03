package com.ecjtu.hht.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author hht
 * @date 2019/12/12 15:45
 */
@Data
@Builder
public class Links {
    @JsonProperty("info")
    private Info info;
}
