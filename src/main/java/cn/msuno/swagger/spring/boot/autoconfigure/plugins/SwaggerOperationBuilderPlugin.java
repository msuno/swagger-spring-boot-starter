package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.MethodJavadoc;
import cn.msuno.javadoc.docs.OtherJavadoc;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Primary
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerOperationBuilderPlugin extends SwaggerBuilderPlugin implements OperationBuilderPlugin {
    
    @Override
    public void apply(OperationContext context) {
        Object requestContext = readValue(context, "requestContext");
        RequestHandler handler = (RequestHandler)readValue(requestContext, "handler");
        Class<?> clz = handler.declaringClass();
        String name = handler.getName();
        ClassJavadoc javadoc = getOrCreate(clz);
        Set<String> tagSet = new HashSet<>();
        String summary = context.getName();
        for (OtherJavadoc other : javadoc.getOther()) {
            if ("tag".equals(other.getName())) {
                tagSet.add(other.getComment().toString());
            }
        }
        if (tagSet.isEmpty()) {
            tagSet.add(context.getGroupName());
        }
        context.operationBuilder().tags(newHashSet(tagSet));
        for (MethodJavadoc methodJavadoc : javadoc.getMethods()) {
            if (name.equals(methodJavadoc.getName()) && handler.getParameters().size() == methodJavadoc.getParams().size()) {
                name = methodJavadoc.getComment().toString();
                for (OtherJavadoc other : methodJavadoc.getOther()) {
                    if ("summary".equals(other.getName())) {
                        summary = other.getComment().toString();
                    }
                }
                break;
            }
        }
        context.operationBuilder().summary(summary).notes(name);
    }
    
    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
