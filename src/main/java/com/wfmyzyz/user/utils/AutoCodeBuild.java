package com.wfmyzyz.user.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.wfmyzyz.user.config.AutoConfig;
import org.springframework.stereotype.Component;

import java.util.*;

/**
* @author auto
*/
@Component
public class AutoCodeBuild {

    public static void main(String[] args) {
        String tableName = "action";
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("auto");
        gc.setFileOverride(false);
        gc.setBaseResultMap(true);
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(AutoConfig.DATA_URL);
        dsc.setDriverName(AutoConfig.DATA_DRIVER);
        dsc.setUsername(AutoConfig.DATA_USERNAME);
        dsc.setPassword(AutoConfig.DATA_PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(AutoConfig.PACK_NAME+"."+AutoConfig.PROJECT_NAME);
        pc.setController("controller.base");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);

        // 模板配置
        TemplateConfig templateConfig = new TemplateConfig().setController("templates/controller/controller.java");
        mpg.setTemplate(templateConfig);

        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("rootPackName",AutoConfig.PACK_NAME);
                this.setMap(map);
            }
        };
        String pageStaticPath = "src/main/resources/static/"+AutoConfig.PROJECT_NAME;
        List<FileOutConfig> focList = new ArrayList<>();
            focList.add(new FileOutConfig("templates/html/list.html.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
            return pageStaticPath+"/"+StringUtils.underlineToCamel(tableName)+"List.html";
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        mpg.setCfg(injectionConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        //表名
        strategy.setInclude(tableName);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
