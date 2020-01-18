package com.wfmyzyz.user.user.controller.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2020-01-09
 */
@RestController
@RequestMapping("base/authority")
public class AuthorityController {

    @Autowired
    IAuthorityService authorityServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
     * @param authorityId
     * @param name
     * @param url
     * @param type
     * @param fAuthorityId
     * @param tbStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "getAuthorityList")
    public LayuiBackData getAuthorityList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                    @RequestParam(value = "authorityId",required = false) String authorityId,
                                                    @RequestParam(value = "name",required = false) String name,
                                                    @RequestParam(value = "url",required = false) String url,
                                                    @RequestParam(value = "type",required = false) String type,
                                                    @RequestParam(value = "fAuthorityId",required = false) String fAuthorityId,
                                                    @RequestParam(value = "tbStatus",required = false) String tbStatus,
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
                    if (!Strings.isBlank(authorityId)){
                        queryWrapper.eq("authority_id",authorityId);
                    }
                    if (!Strings.isBlank(name)){
                        queryWrapper.like("name",name);
                    }
                    if (!Strings.isBlank(url)){
                        queryWrapper.like("url",url);
                    }
                    if (!Strings.isBlank(type)){
                        queryWrapper.like("type",type);
                    }
                    if (!Strings.isBlank(fAuthorityId)){
                        queryWrapper.eq("f_authority_id",fAuthorityId);
                    }
                    if (!Strings.isBlank(tbStatus)){
                        queryWrapper.eq("tb_status",tbStatus);
                    }
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<Authority> pageList = authorityServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg getAuthorityOne(@PathVariable("id") String id){
        Authority authority = authorityServiceImpl.getById(id);
        if(authority == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",authority);
    }

    /**
    * 添加
    * @param authority
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid Authority authority, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = authorityServiceImpl.save(authority);
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param authority
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid Authority authority, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = authorityServiceImpl.updateById(authority);
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
        Authority authority = authorityServiceImpl.getById(id);
        authority.setTbStatus("删除");
        boolean flag = authorityServiceImpl.updateById(authority);
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param authorityList
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<Authority> authorityList){
    List<Authority> deleteAuthorityList = new ArrayList<>();
        for (Authority authority:authorityList){
            authority.setTbStatus("删除");
            deleteAuthorityList.add(authority);
        }
        boolean flag = authorityServiceImpl.updateBatchById(deleteAuthorityList);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}