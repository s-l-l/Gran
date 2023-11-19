package com.shill.gran.common.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
@Data
public class ModifyNameReq {
    @NotNull
    @Length(max = 6, message = "用户名可别取太长，不然我记不住噢")
    @ApiModelProperty("用户名")
    private String name;
}
