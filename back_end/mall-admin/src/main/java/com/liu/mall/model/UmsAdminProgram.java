package com.liu.mall.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
public class UmsAdminProgram {
    @NotEmpty
    @ApiModelProperty(value = "用户名",required = true)
    public String username;

    @NotEmpty
    @ApiModelProperty(value = "密码",required = true)
    public String password;
}
