package io.gr1d.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel
@Getter
public class CreatedResponse {

    @ApiModelProperty(notes = "The recently created model's identification")
    private final String uuid;

    public CreatedResponse(final String uuid) {
        this.uuid = uuid;
    }

}
