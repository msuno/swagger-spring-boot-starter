package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.OtherJavadoc;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;

@Component
@Primary
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerApiListingBuilderPlugin extends SwaggerBuilderPlugin implements ApiListingBuilderPlugin {
    
    @Override
    public void apply(ApiListingContext apiListingContext) {
        Class<?> controllerClass = apiListingContext.getResourceGroup().getControllerClass().get();
        ClassJavadoc javadoc = getOrCreate(controllerClass);
        String description = javadoc.getComment().toString();
        Set<String> tagSet = new HashSet<>();
        for (OtherJavadoc other : javadoc.getOther()) {
            if ("tag".equals(other.getName())) {
                tagSet.add(other.getComment().toString());
            }
        }
        if (tagSet.isEmpty()) {
            tagSet.add(apiListingContext.getResourceGroup().getGroupName());
        }
        apiListingContext.apiListingBuilder().description(description).tagNames(tagSet);
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
