package com.wfmyzyz.user.user.controller.base;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.user.domain.UserRole;
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
 * @since 2019-12-30
 */
@RestController
@RequestMapping("base/userRole")
public class UserRoleController {

    @Autowired
    IUserRoleService userRoleServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param userRoleId
     * @param userId
     * @param roleId
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getUserRoleList")
    public LayuiBackData getUserRoleList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "userRoleId",required = false) String userRoleId,
                                                    @RequestParam(value = "userId",required = false) String userId,
                                                    @RequestParam(value = "roleId",required = false) String roleId,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(userRoleId)){
                        queryWrapper.eq("user_role_id",userRoleId);
                    }
                    if (!Strings.isBlank(userId)){
                        queryWrapper.eq("user_id",userId);
                    }
                    if (!Strings.isBlank(roleId)){
                        queryWrapper.eq("role_id",roleId);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<UserRole> pageList = userRoleServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getUserRoleOne(@PathVariable("id") String id){
        UserRole userRole = userRoleServiceImpl.getById(id);
        if(userRole == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",userRole);
    }

    /**
    * 添加
    * @param userRole
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid UserRole userRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = userRoleServiceImpl.save(userRole);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param userRole
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid UserRole userRole, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = userRoleServiceImpl.updateById(userRole);
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
        UserRole userRole = userRoleServiceImpl.getById(id);
        userRole.setTbStatus("删除");
        boolean flag = userRoleServiceImpl.updateById(userRole);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param userRoleList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<UserRole> userRoleList){
    List<UserRole> deleteUserRoleList = new ArrayList<>();
        for (UserRole userRole:userRoleList){
            userRole.setTbStatus("删除");
            deleteUserRoleList.add(userRole);
        }
        boolean flag = userRoleServiceImpl.updateBatchById(deleteUserRoleList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}