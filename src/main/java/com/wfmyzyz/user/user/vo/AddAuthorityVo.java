package com.wfmyzyz.user.user.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
public class AddAuthorityVo {

    @NotBlank(message = "权限名不能为空")
    private String name;

    private String url;

    @NotBlank(message = "权限类型不能为空")
    private String type;

    @NotNull(message = "父权限ID不能为空")
    private Integer fAuthorityId;

    private Integer sort;

    private String display;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getfAuthorityId() {
        return fAuthorityId;
    }

    public void setfAuthorityId(Integer fAuthorityId) {
        this.fAuthorityId = fAuthorityId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "AddAuthorityVo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", fAuthorityId=" + fAuthorityId +
                ", sort=" + sort +
                ", display='" + display + '\'' +
                '}';
    }
}
