package com.wfmyzyz.user.user.vo;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 */
public class AddUserVo {

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证密码不能为空")
    private String passwords;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    @Override
    public String toString() {
        return "AddUserVo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwords='" + passwords + '\'' +
                '}';
    }
}
