package cn.msuno.swagger.spring.boot.autoconfigure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cn.msuno.swagger.spring.boot.autoconfigure.model.CustomDef;
import cn.msuno.swagger.spring.boot.autoconfigure.model.CustomPage;
import cn.msuno.swagger.spring.boot.autoconfigure.properties.SwaggerProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.OperationParameterReader;

/**
 * 配置swagger必要参数
 *
 * @author msuno
 * @version 1.0.0
 */
@Component
@EnableConfigurationProperties(SwaggerProperties.class)
@ComponentScan(basePackages = {
        "cn.msuno.swagger.spring.boot.autoconfigure.plugins",
        "cn.msuno.swagger.spring.boot.autoconfigure.web",
        "cn.msuno.swagger.spring.boot.autoconfigure.configuration"
})
@ConditionalOnClass(value = {PluginRegistry.class, OperationParameterReader.class})
@ConditionalOnWebApplication
public class SwaggerAutoConfiguration {
    
    private static Logger log = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
    
    /**
     * 注入默认配置
     */
    @Autowired
    private SwaggerProperties swaggerProperties;
   
    @Autowired
    ServletContext servletContext;
    
    /**
     * 如果用户没有配置Docket，定义一个默认Docket
     * @return Docket.class
     */
    @Bean
    @ConditionalOnMissingBean(name = "docket")
    public Docket createDocket() {
        log.info("auto config swagger");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("msuno")
                .enable(swaggerProperties.isEnable())
                .apiInfo(new ApiInfoBuilder().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .termsOfServiceUrl(swaggerProperties.getTermsOfService())
                        .version(swaggerProperties.getVersion())
                        .contact(swaggerProperties.getContact())
                        .license(swaggerProperties.getLicense())
                        .build())
                .pathProvider(new RelativePathProvider(servletContext){
                    @Override
                    protected String applicationPath() {
                        if (StringUtils.isNotBlank(swaggerProperties.getBasePath())) {
                            return swaggerProperties.getBasePath();
                        }
                        return super.applicationPath();
                    }
    
                    @Override
                    protected String getDocumentationPath() {
                        if (StringUtils.isNotBlank(swaggerProperties.getBasePath())) {
                            return swaggerProperties.getBasePath();
                        }
                        return super.getDocumentationPath();
                    }
                })
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalParameters());
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "responseCode")
    public Map<String, String> responseCode() {
        return new HashMap<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "globalParameters")
    public List<Parameter> globalParameters() {
        return new ArrayList<>();
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "customPage")
    public List<CustomPage> customPage() {
        return Lists.newArrayList();
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "customDef")
    public List<CustomDef> customDef() {
        return Lists.newArrayList();
    }
}
