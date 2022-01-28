package com.chuncongcong.crm.common.config.swagger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 * @author HU
 * @date 2021/3/5 11:50
 */

@EnableKnife4j
@EnableOpenApi
@Configuration
public class Swagger3Config implements WebMvcConfigurer {

	public static final String AUTHORIZATION_HEADER = "token";

	@Bean
	public Docket createRestApi() {
		//返回文档摘要信息
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo())
				.securityContexts(securityContexts())
				.securitySchemes(Collections.singletonList(apiKey()))
				.select()
				//.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey(AUTHORIZATION_HEADER , AUTHORIZATION_HEADER, "header");
	}

	public List<SecurityContext> securityContexts(){
		List<SecurityContext> securityContexts=new ArrayList<>();
		securityContexts.add(SecurityContext.builder()
				.securityReferences(securityReferences())
				.forPaths(PathSelectors.any()).build());

		return securityContexts;
	}

	public List<SecurityReference> securityReferences(){
		AuthorizationScope[] authorizationScopes=new AuthorizationScope[1];
		authorizationScopes[0]=new AuthorizationScope("global","accessEverything");
		List<SecurityReference> securityReferences=new ArrayList<>();
		securityReferences.add(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
		return securityReferences;
	}


	//生成接口信息，包括标题、联系人等
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("crm-接口文档")
				.version("1.0")
				.build();
	}

	//生成全局通用参数
	private List<RequestParameter> getGlobalRequestParameters() {
		List<RequestParameter> parameters = new ArrayList<>();
		parameters.add(new RequestParameterBuilder()
				.name("appid")
				.description("平台id")
				.required(true)
				.in(ParameterType.QUERY)
				.query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
				.required(false)
				.build());
		return parameters;
	}
}
