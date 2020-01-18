package com.wfmyzyz.user.user.controller.base;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.user.domain.RoleAuthority;
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
@RequestMapping("base/roleAuthority")
public class RoleAuthorityController {

    @Autowired
    IRoleAuthorityService roleAuthorityServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param roleAuthorityId
     * @param roleId
     * @param authorityId
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getRoleAuthorityList")
    public LayuiBackData getRoleAuthorityList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "roleAuthorityId",required = false) String roleAuthorityId,
                                                    @RequestParam(value = "roleId",required = false) String roleId,
                                                    @RequestParam(value = "authorityId",required = false) String authorityId,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(roleAuthorityId)){
                        queryWrapper.eq("role_authority_id",roleAuthorityId);
                    }
                    if (!Strings.isBlank(roleId)){
                        queryWrapper.eq("role_id",roleId);
                    }
                    if (!Strings.isBlank(authorityId)){
                        queryWrapper.eq("authority_id",authorityId);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<RoleAuthority> pageList = roleAuthorityServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getRoleAuthorityOne(@PathVariable("id") String id){
        RoleAuthority roleAuthority = roleAuthorityServiceImpl.getById(id);
        if(roleAuthority == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",roleAuthority);
    }

    /**
    * 添加
    * @param roleAuthority
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid RoleAuthority roleAuthority, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = roleAuthorityServiceImpl.save(roleAuthority);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param roleAuthority
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid RoleAuthority roleAuthority, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = roleAuthorityServiceImpl.updateById(roleAuthority);
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
        RoleAuthority roleAuthority = roleAuthorityServiceImpl.getById(id);
        roleAuthority.setTbStatus("删除");
        boolean flag = roleAuthorityServiceImpl.updateById(roleAuthority);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param roleAuthorityList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<RoleAuthority> roleAuthorityList){
    List<RoleAuthority> deleteRoleAuthorityList = new ArrayList<>();
        for (RoleAuthority roleAuthority:roleAuthorityList){
            roleAuthority.setTbStatus("删除");
            deleteRoleAuthorityList.add(roleAuthority);
        }
        boolean flag = roleAuthorityServiceImpl.updateBatchById(deleteRoleAuthorityList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}