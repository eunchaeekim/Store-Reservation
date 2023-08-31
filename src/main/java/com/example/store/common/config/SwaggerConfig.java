package com.example.store.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

// @Configuration 어노테이션은 Spring 컨텍스트에서 Bean 구성을 나타내는 클래스임을 나타냅니다.
@Configuration
public class SwaggerConfig {

    // Swagger 문서를 생성하는 Docket Bean을 생성하는 메서드입니다.
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 컨트롤러의 기본 패키지를 지정하여 해당 패키지에 속한 API만 문서화합니다.
                .apis(RequestHandlerSelectors.basePackage("com.example"))
                // 모든 경로를 문서화합니다.
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    // API 문서 정보를 설정하는 메서드입니다.
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // API 문서의 제목을 설정합니다.
                .title("Store API")
                // API 문서에 대한 간단한 설명을 설정합니다.
                .description("식당 예약 시스템입니다.")
                // API 문서의 버전 정보를 설정합니다.
                .version("0.1")
                .build();
    }

    // 클라이언트가 서버에게 어떤 데이터 형식으로 데이터를 전송할 것인지를 정의
    // -> JSON 형식과 폼 데이터(application/x-www-form-urlencoded)를 사용하여 데이터를 전송하도록 설정
    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    // 서버가 클라이언트에게 제공하는 데이터 형식을 설정하는 역할 -> JSON 형식으로 데이터를 생성하도록 설정
    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

}
