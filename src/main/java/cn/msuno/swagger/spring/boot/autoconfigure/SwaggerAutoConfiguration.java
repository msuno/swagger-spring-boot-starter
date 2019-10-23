package cn.msuno.swagger.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import cn.msuno.swagger.spring.boot.autoconfigure.model.StatusCode;
import cn.msuno.swagger.spring.boot.autoconfigure.properties.SwaggerProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.OperationParameterReader;

/**
 * 配置swagger必要参数
 *
 * @author msuno
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ComponentScan(basePackages = {
        "cn.msuno.swagger.spring.boot.autoconfigure"
})
@ConditionalOnClass(value = {PluginRegistry.class, OperationParameterReader.class})
public class SwaggerAutoConfiguration {
    
    private static Logger log = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
    
    /**
     * 注入默认配置
     */
    @Autowired
    private SwaggerProperties swaggerProperties;
   
    /**
     * 如果用户没有配置Docket，定义一个默认Docket
     * @return Docket.class
     */
    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket createDocket() {
        log.info("auto config swagger");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                        .version(swaggerProperties.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
    
    @Bean
    @ConditionalOnMissingBean(value = StatusCode.class)
    public StatusCode statusCode() {
        return new StatusCode();
    }
    
}
