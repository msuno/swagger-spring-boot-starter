## swagger-spring-boot-starter

+ 引入pom
```xml
<dependency>
    <groupId>cn.msuno</groupId>
    <artifactId>swagger-spring-boot-starter</artifactId>
    <version>2.3.0</version>
</dependency>
```

+ 启动javadoc swagger restful, 如果注解EnableJavadocSwagger2
```java
@EnableJavadocSwagger2  //启动javadoc swagger
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
```

+ 配置全局状态码
```java

@Configuration
public class Swagger2  {
    /**
     * 全局状态码key, value形式
     * code-desc:  200-OK
     */
    @Bean(name = "responseCode")
    public Map<String, String> responseCode(){
        Map<String,String> result = new HashMap<>();
        return result;
    }
    
}
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
