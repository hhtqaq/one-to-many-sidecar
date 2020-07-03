package com.ecjtu.hht.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hht
 * @date 2019/12/5 17:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {
    @JsonProperty("version")
    private String version;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("artifactId")
    private String artifactId;
}
