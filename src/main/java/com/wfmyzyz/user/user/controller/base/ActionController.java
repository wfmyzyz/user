package com.wfmyzyz.user.user.controller.base;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.wfmyzyz.user.user.service.IActionService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.user.domain.Action;
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
 * @since 2020-02-28
 */
@RestController
@RequestMapping("base/action")
public class ActionController {

    @Autowired
    IActionService actionServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param actionId
     * @param title
     * @param token
     * @param result
     * @param ip
     * @param username
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getActionList")
    public LayuiBackData getActionList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "actionId",required = false) String actionId,
                                                    @RequestParam(value = "title",required = false) String title,
                                                    @RequestParam(value = "token",required = false) String token,
                                                    @RequestParam(value = "result",required = false) String result,
                                                    @RequestParam(value = "ip",required = false) String ip,
                                                    @RequestParam(value = "username",required = false) String username,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(actionId)){
                        queryWrapper.eq("action_id",actionId);
                    }
                    if (!Strings.isBlank(title)){
                        queryWrapper.like("title",title);
                    }
                    if (!Strings.isBlank(token)){
                        queryWrapper.like("token",token);
                    }
                    if (!Strings.isBlank(result)){
                        queryWrapper.like("result",result);
                    }
                    if (!Strings.isBlank(ip)){
                        queryWrapper.like("ip",ip);
                    }
                    if (!Strings.isBlank(username)){
                        queryWrapper.like("username",username);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<Action> pageList = actionServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getActionOne(@PathVariable("id") String id){
        Action action = actionServiceImpl.getById(id);
        if(action == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",action);
    }

    /**
    * 添加
    * @param action
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid Action action, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = actionServiceImpl.save(action);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param action
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid Action action, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = actionServiceImpl.updateById(action);
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
        Action action = actionServiceImpl.getById(id);
        action.setTbStatus("删除");
        boolean flag = actionServiceImpl.updateById(action);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param actionList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<Action> actionList){
    List<Action> deleteActionList = new ArrayList<>();
        for (Action action:actionList){
            action.setTbStatus("删除");
            deleteActionList.add(action);
        }
        boolean flag = actionServiceImpl.updateBatchById(deleteActionList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}