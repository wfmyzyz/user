package com.wfmyzyz.user.user.vo;

import com.wfmyzyz.user.user.domain.Authority;

import java.util.List;

/**
 * @author admin
 */
public class AuthorityTreeVo {
    private Authority authority;
    private List<AuthorityTreeVo> children;

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public List<AuthorityTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<AuthorityTreeVo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "AuthorityTreeVo{" +
                "authority=" + authority +
                ", children=" + children +
                '}';
    }
}
