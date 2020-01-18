package com.wfmyzyz.user.user.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
public class AddRoleVo {

    @NotBlank(message = "角色名不能为空")
    private String name;
    @NotNull(message = "父角色ID不能为空")
    private Integer fRoleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getfRoleId() {
        return fRoleId;
    }

    public void setfRoleId(Integer fRoleId) {
        this.fRoleId = fRoleId;
    }

    @Override
    public String toString() {
        return "AddRoleVo{" +
                "name='" + name + '\'' +
                ", fRoleId=" + fRoleId +
                '}';
    }
}
