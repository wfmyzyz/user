package com.wfmyzyz.user.user.vo;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
public class UpdateAuthorityVo {

    @NotNull(message = "权限ID不能为空")
    private Integer authorityId;

    private String authorityName;

    private String authorityUrl;

    private String authorityType;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityUrl() {
        return authorityUrl;
    }

    public void setAuthorityUrl(String authorityUrl) {
        this.authorityUrl = authorityUrl;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    @Override
    public String toString() {
        return "UpdateAuthorityVo{" +
                "authorityId=" + authorityId +
                ", authorityName='" + authorityName + '\'' +
                ", authorityUrl='" + authorityUrl + '\'' +
                ", authorityType='" + authorityType + '\'' +
                '}';
    }
}
