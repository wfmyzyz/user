package com.wfmyzyz.user.user.vo;

import java.util.List;

/**
 * @author admin
 */
public class DTree {
    private Integer code;
    private String msg;
    private List<TreeVo> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TreeVo> getData() {
        return data;
    }

    public void setData(List<TreeVo> data) {
        this.data = data;
    }

    public static DTree successRoleTree(List<TreeVo> treeVoList){
        DTree dTree = new DTree();
        dTree.setData(treeVoList);
        dTree.setCode(200);
        dTree.setMsg("操作成功");
        return dTree;
    }

    @Override
    public String toString() {
        return "DTree{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
