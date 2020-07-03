package com.ecjtu.hht.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author hht
 * @date 2019/12/12 15:44
 */
@Data
@AllArgsConstructor
@Builder
public class ActuatorInfo {

    @JsonProperty("_links")
    private Links links;

}
