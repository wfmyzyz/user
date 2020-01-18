package com.wfmyzyz.user.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2019-12-30
 */
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @TableId(value = "authority_id", type = IdType.AUTO)
    private Integer authorityId;

    /**
     * 权限名
     */
    private String name;

    /**
     * 权限地址
     */
    private String url;

    /**
     * 权限类型：页面，按钮
     */
    private String type;

    /**
     * 父权限ID
     */
    private Integer fAuthorityId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态:正常，删除
     */
    @TableLogic
    private String tbStatus;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }
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
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Integer getFAuthorityId() {
        return fAuthorityId;
    }

    public void setFAuthorityId(Integer fAuthorityId) {
        this.fAuthorityId = fAuthorityId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityId=" + authorityId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", fAuthorityId=" + fAuthorityId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tbStatus='" + tbStatus + '\'' +
                '}';
    }
}
