package com.zero.srd.core;

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
* URL: http://localhost:8080/SpringRestDemo/swagger-ui.html
*/
@Configuration
@EnableSwagger2
public class Swagger2Conf {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zero.srd.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot Demo API DOC")
                .description("SpringBoot Demo API Desc")
                .termsOfServiceUrl("http://localhost:8080/SpringBootDemo")                
                .version("1.0")
                .license("SpringBoot Demo License")
                .licenseUrl("dummy/list")
                .build();
    }
}