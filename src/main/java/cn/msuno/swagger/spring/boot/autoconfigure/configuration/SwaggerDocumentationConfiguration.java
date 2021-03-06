package cn.msuno.swagger.spring.boot.autoconfigure.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import cn.msuno.swagger.spring.boot.autoconfigure.mappers.ServiceModelToSwagger2Mapper;
import cn.msuno.swagger.spring.boot.autoconfigure.model.CustomDef;
import cn.msuno.swagger.spring.boot.autoconfigure.model.CustomPage;
import cn.msuno.swagger.spring.boot.autoconfigure.properties.Swagger2JacksonModule;
import cn.msuno.swagger.spring.boot.autoconfigure.web.Swagger2Controller;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;

@Component
@Import({ SpringfoxWebMvcConfiguration.class, SwaggerCommonConfiguration.class })
@ComponentScan(basePackages = {
        "cn.msuno.swagger.spring.boot.autoconfigure.mappers"
})
@ConditionalOnWebApplication
public class SwaggerDocumentationConfiguration {
    @Bean
    public JacksonModuleRegistrar swagger2Module() {
        return new Swagger2JacksonModule();
    }
    
    @Bean
    public HandlerMapping swagger2ControllerMapping(
            Environment environment,
            DocumentationCache documentationCache,
            ServiceModelToSwagger2Mapper mapper,
            JsonSerializer jsonSerializer,
            Map<String, String> responseCode, List<CustomPage> customPage, List<CustomDef> customDef) {
        return new PropertySourcedRequestMappingHandlerMapping(
                environment,
                new Swagger2Controller(environment, documentationCache, mapper, jsonSerializer, responseCode, customPage, customDef));
    }
}
