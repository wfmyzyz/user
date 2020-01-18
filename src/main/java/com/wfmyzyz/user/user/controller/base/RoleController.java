package com.wfmyzyz.user.user.controller.base;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.user.domain.Role;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.logging.log4j.util.Strings;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2019-12-27
 */
@RestController
@RequestMapping("base/role")
public class RoleController {

    @Autowired
    IRoleService roleServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param roleId
     * @param name
     * @param fRoleId
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getRoleList")
    public LayuiBackData getRoleList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "roleId",required = false) String roleId,
                                                    @RequestParam(value = "name",required = false) String name,
                                                    @RequestParam(value = "fRoleId",required = false) String fRoleId,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(roleId)){
                        queryWrapper.eq("role_id",roleId);
                    }
                    if (!Strings.isBlank(name)){
                        queryWrapper.like("name",name);
                    }
                    if (!Strings.isBlank(fRoleId)){
                        queryWrapper.eq("f_role_id",fRoleId);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<Role> pageList = roleServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getRoleOne(@PathVariable("id") String id){
        Role role = roleServiceImpl.getById(id);
        if(role == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",role);
    }

    /**
    * 添加
    * @param role
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid Role role, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = roleServiceImpl.save(role);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param role
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid Role role, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = roleServiceImpl.updateById(role);
        if (!flag){
            return Msg.error().add("error","修改失败！请重新修改");
        }
        return Msg.success().add("data","修改成功！");
    }

    /**
    * 删除一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "remove/{id}",method = RequestMethod.GET)
    public Msg remove(@PathVariable("id") String id){
        Role role = roleServiceImpl.getById(id);
        role.setTbStatus("删除");
        boolean flag = roleServiceImpl.updateById(role);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param roleList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<Role> roleList){
    List<Role> deleteRoleList = new ArrayList<>();
        for (Role role:roleList){
            role.setTbStatus("删除");
            deleteRoleList.add(role);
        }
        boolean flag = roleServiceImpl.updateBatchById(deleteRoleList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}