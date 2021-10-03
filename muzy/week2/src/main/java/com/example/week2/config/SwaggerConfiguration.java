package com.example.week2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket swaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.week2.controller")) //apis 경로는 컨트롤러가 존재하는 경로로 변경
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }
    private ApiInfo swaggerInfo(){
        return new ApiInfoBuilder().title("Spring API Documentation")
                .description("웹 개발시 사용되는 서버 API에 대한 연동 문서")
                .license("").licenseUrl("").version("1").build();
    }
}
