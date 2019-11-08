package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.MethodJavadoc;
import cn.msuno.javadoc.docs.ParamJavadoc;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerParameterBuilderPlugin extends SwaggerBuilderPlugin implements ParameterBuilderPlugin {
    
    @Override
    public void apply(ParameterContext parameterContext) {
        Object requestContext = readValue(parameterContext.getOperationContext(), "requestContext");
        RequestHandler handler = (RequestHandler)readValue(requestContext, "handler");
        Class<?> clz = handler.declaringClass();
        String name = handler.getName();
        ClassJavadoc javadoc = getOrCreate(clz);
        String parameterName = parameterContext.resolvedMethodParameter().defaultName().orNull();
        if (Objects.isNull(parameterName)) {
            return ;
        }
        String description = parameterName;
        boolean required = false;
        String defaultValue = "";
        for (MethodJavadoc methodJavadoc : javadoc.getMethods()) {
            if (name.equals(methodJavadoc.getName())) {
                for (ParamJavadoc paramJavadoc : methodJavadoc.getParams()){
                    if (parameterName.equals(paramJavadoc.getName())) {
                        parameterName = paramJavadoc.getName();
                        String string = paramJavadoc.getComment().toString();
                        if (StringUtils.isBlank(string)){
                            break;
                        }
                        String[] split = string.split("\\s+");
                        description = split[0];
                        for (int i = 1 ; i < split.length ; i ++) {
                            if ("required".equals(split[i])) {
                                required = true;
                            }
                            if (split[i].startsWith("e_")) {
                                defaultValue = split[i].replace("e_", "");
                            }
                        }
                    }
                }
                break;
            }
        }
        parameterContext.parameterBuilder().name(parameterName).description(description)
                .required(required).defaultValue(defaultValue);
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
    
    
}
