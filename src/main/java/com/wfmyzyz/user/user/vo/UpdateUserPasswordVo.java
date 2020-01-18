package com.wfmyzyz.user.user.vo;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 */
public class UpdateUserPasswordVo {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @NotBlank(message = "新验证密码不能为空")
    private String newPasswords;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswords() {
        return newPasswords;
    }

    public void setNewPasswords(String newPasswords) {
        this.newPasswords = newPasswords;
    }

    @Override
    public String toString() {
        return "UpdateUserPasswordVo{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPasswords='" + newPasswords + '\'' +
                '}';
    }
}
