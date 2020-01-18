package com.wfmyzyz.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author admin
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket baseApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("模板接口").apiInfo(baseInfo()).select().apis(RequestHandlerSelectors.basePackage("com.wfmyzyz.user"))
                .paths(PathSelectors.regex(".*/base/.*")).build();
    }
    private ApiInfo baseInfo() {
        return new ApiInfoBuilder().title("模板接口文档").description("模板接口文档").termsOfServiceUrl("").version("1.0").build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("API管理接口").apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.wfmyzyz.user"))
                .paths(PathSelectors.regex(".*/api/.*")).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("API接口文档").description("API接口文档").termsOfServiceUrl("").version("1.0").build();
    }

    @Bean
    public Docket backApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("后台管理接口").apiInfo(backInfo()).select().apis(RequestHandlerSelectors.basePackage("com.wfmyzyz.user"))
                .paths(PathSelectors.regex(".*/back/.*")).build();
    }
    private ApiInfo backInfo() {
        return new ApiInfoBuilder().title("后台接口文档").description("后台接口文档").termsOfServiceUrl("").version("1.0").build();
    }
}
