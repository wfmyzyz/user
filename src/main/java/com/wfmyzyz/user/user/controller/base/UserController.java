package com.wfmyzyz.user.user.controller.base;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.user.domain.User;
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
 * @since 2019-12-19
 */
@RestController
@RequestMapping("base/user")
public class UserController {

    @Autowired
    IUserService userServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param userId
     * @param username
     * @param password
     * @param salt
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getUserList")
    public LayuiBackData getUserList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "userId",required = false) String userId,
                                                    @RequestParam(value = "username",required = false) String username,
                                                    @RequestParam(value = "password",required = false) String password,
                                                    @RequestParam(value = "salt",required = false) String salt,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(userId)){
                        queryWrapper.eq("user_id",userId);
                    }
                    if (!Strings.isBlank(username)){
                        queryWrapper.like("username",username);
                    }
                    if (!Strings.isBlank(password)){
                        queryWrapper.like("password",password);
                    }
                    if (!Strings.isBlank(salt)){
                        queryWrapper.like("salt",salt);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<User> pageList = userServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getUserOne(@PathVariable("id") String id){
        User user = userServiceImpl.getById(id);
        if(user == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",user);
    }

    /**
    * 添加
    * @param user
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = userServiceImpl.save(user);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param user
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = userServiceImpl.updateById(user);
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
        User user = userServiceImpl.getById(id);
        user.setTbStatus("删除");
        boolean flag = userServiceImpl.updateById(user);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param userList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<User> userList){
    List<User> deleteUserList = new ArrayList<>();
        for (User user:userList){
            user.setTbStatus("删除");
            deleteUserList.add(user);
        }
        boolean flag = userServiceImpl.updateBatchById(deleteUserList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}