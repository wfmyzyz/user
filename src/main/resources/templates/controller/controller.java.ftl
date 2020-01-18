package ${package.Controller};

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ${package.Service}.${table.serviceName};
import ${cfg.rootPackName}.utils.LayuiBackData;
import ${cfg.rootPackName}.utils.Msg;
import ${package.Entity}.${table.entityName};
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
 * @since ${date}
 */
@RestController
@RequestMapping("base/${table.entityPath}")
public class ${table.entityName}Controller {

    @Autowired
    ${table.serviceName} ${table.entityPath}ServiceImpl;

    /**
     * 查询列表
     * @param page
     * @param limit
<#list table.fields as field>
    <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
     * @param ${field.propertyName}
     </#if>
</#list>
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "get${table.entityName}List")
    public LayuiBackData get${table.entityName}List(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                            <#list table.fields as field>
                                                <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
                                                    @RequestParam(value = "${field.propertyName}",required = false) String ${field.propertyName},
                                                </#if>
                                            </#list>
                                                    @RequestParam(value = "startTime",required = false) String startTime,
                                                    @RequestParam(value = "endTime",required = false) String endTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        <#list table.fields as field>
            <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
                <#if field.columnType == "INTEGER" || field.columnType == "LONG" || field.columnType == "SHORT" || field.propertyName == "tbStatus">
                    if (!Strings.isBlank(${field.propertyName})){
                        queryWrapper.eq("${field.name}",${field.propertyName});
                    }
                <#else>
                    if (!Strings.isBlank(${field.propertyName})){
                        queryWrapper.like("${field.name}",${field.propertyName});
                    }
                </#if>
            </#if>
        </#list>
        if(!Strings.isBlank(startTime) && !Strings.isBlank(endTime)){
            queryWrapper.between("create_time",startTime,endTime);
        }
        IPage<${table.entityName}> pageList = ${table.entityPath}ServiceImpl.page(new Page<>(page,limit),queryWrapper);
        return  LayuiBackData.bringData(pageList);
    }


    /**
    * 获取一条数据
    * @param id
    * @return
    */
    @RequestMapping(value = "get/{id}")
    public Msg get${table.entityName}One(@PathVariable("id") String id){
        ${table.entityName} ${table.entityPath} = ${table.entityPath}ServiceImpl.getById(id);
        if(${table.entityPath} == null){
            return Msg.error().add("error","查询为空！");
        }
            return Msg.success().add("data",${table.entityPath});
    }

    /**
    * 添加
    * @param ${table.entityPath}
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Msg add(@Valid ${table.entityName} ${table.entityPath}, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = ${table.entityPath}ServiceImpl.save(${table.entityPath});
        if (!flag){
            return Msg.error().add("error","添加失败！请重新添加");
        }
        return  Msg.success().add("data","添加成功！");
    }

    /**
    * 修改
    * @param ${table.entityPath}
    * @param bindingResult
    * @return
    */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Msg update(@Valid ${table.entityName} ${table.entityPath}, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return Msg.resultError(bindingResult);
        }
        boolean flag = ${table.entityPath}ServiceImpl.updateById(${table.entityPath});
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
        ${table.entityName} ${table.entityPath} = ${table.entityPath}ServiceImpl.getById(id);
        ${table.entityPath}.setTbStatus("删除");
        boolean flag = ${table.entityPath}ServiceImpl.updateById(${table.entityPath});
        if(!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
    * 批量删除
    * @param ${table.entityPath}List
    * @return
    */
    @RequestMapping(value = "remove",method = RequestMethod.POST)
    public Msg removeBatch(@RequestBody List<${table.entityName}> ${table.entityPath}List){
    List<${table.entityName}> delete${table.entityName}List = new ArrayList<>();
        for (${table.entityName} ${table.entityPath}:${table.entityPath}List){
            ${table.entityPath}.setTbStatus("删除");
            delete${table.entityName}List.add(${table.entityPath});
        }
        boolean flag = ${table.entityPath}ServiceImpl.updateBatchById(delete${table.entityName}List);
        if (!flag){
            return Msg.error().add("error","删除失败！");
        }
        return Msg.success().add("data","删除成功！");
    }
}