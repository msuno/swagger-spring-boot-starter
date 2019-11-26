## swagger-spring-mvc

+ 引入pom
```xml
<dependency>
  <groupId>cn.msuno</groupId>
  <artifactId>swagger-spring-mvc</artifactId>
  <version>spring-mvc-1.0.0</version>
</dependency>
```

+ 启动javadoc swagger restful
```java
package com.live.web.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

import cn.msuno.swagger.spring.boot.autoconfigure.model.CustomPage;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan(basePackages = "cn.msuno.swagger.spring.boot.autoconfigure")
public class SwaggerConfig {
    
    private  Logger logger = LoggerFactory.getLogger(getClass());
   
    @Bean("docket")
    @Primary
    public Docket createRestApi() {
        // 添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Content-Type").description("ContentType").modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue(MediaType.APPLICATION_JSON_UTF8_VALUE).required(true).build();
        pars.add(tokenPar.build());
        tokenPar.name("cloud-meeting-client-type").description("请求类型").modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue("wxapp").required(true).build();
        pars.add(tokenPar.build());
        tokenPar.name("cloud-meeting-session-id").description("sessionId").modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue("").required(true).build();
        pars.add(tokenPar.build());
        //添加head参数end
        
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.live.web.controller.api"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云会议API")
                .description("小程序、APP和PC网页数据接接口")
                .version("1.0")
                .build();
    }
    
    @Bean(name = "responseCode")
    public Map<String, String> responseCode(){
        return new HashMap<>();
    }
    
    @Bean(name = "customPage")
    public List<CustomPage> cusTomPage() {
        return new ArrayList<>();
    }
    
}
```

+ 配置静态资源
```xml
<mvc:resources mapping="/doc.html" location="classpath:/META-INF/resources/" />
<mvc:resources mapping="/webjars/**" location="classpath:/META-INF/resources/webjars/" />
```


+ 增加全局入参，参考[com.live.cloudmeeting.bootconfig.Swagger2]

## Javadoc Swagger usage
+ Javadoc书写要规范，Java会自动读取java构建swagger.json数据
+ Javadoc 入参与出参
>入参body语法
```java
public class CheckPassWordReq {
    /**
     * 房间号
     * @required true  是否必须
     * @default 1      默认值
     */
    private Integer roomNo;
}
```
>入参query语法
```java

/**
 * Test接口           接口描述
 * @tag Test接口      @tag 为接口组列表名
 * @author msuno
 */
@RestController
@RequestMapping(value = "/api/test",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController extends BaseRestController {
    /**
     * 小程序绑定手机号                         接口详情描述
     * @param name 姓名 required e_10          @param 入参 说明 required(有该字段必须参数) e_10(e_前缀为example，e_后面为列子)
     * @param password 密码 required e_use3
     * @param key 主键 e_8hs
     * @summary 小程序绑定手机号                @summary 接口列表显示
     */
    @ResponseBody
    @PostMapping(value = "/bindMobile")
    public CodeResp<BindMobileResp> bindMobile(String name, String password, String key) {
        return OK();
    }
}
```
>出参，出参不需要和入参body语法一样

