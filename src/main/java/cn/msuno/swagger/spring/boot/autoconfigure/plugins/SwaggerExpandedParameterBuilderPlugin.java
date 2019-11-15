package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.transform;
import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.containerType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Types.typeNameFor;
import static springfox.documentation.service.Parameter.DEFAULT_PRECEDENCE;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.FieldJavadoc;
import springfox.documentation.schema.Enums;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.ParameterMetadataAccessor;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerExpandedParameterBuilderPlugin extends SwaggerBuilderPlugin implements ExpandedParameterBuilderPlugin {
    
    private final TypeResolver resolver;
    private final EnumTypeDeterminer enumTypeDeterminer;
    
    @Autowired
    public SwaggerExpandedParameterBuilderPlugin(
            TypeResolver resolver,
            EnumTypeDeterminer enumTypeDeterminer) {
        this.resolver = resolver;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }
    
    
    @Override
    public void apply(ParameterExpansionContext context) {
        AllowableValues allowable = allowableValues(context.getFieldType().getErasedType());
        ParameterMetadataAccessor metadataAccessor = (ParameterMetadataAccessor) readValue(context, "metadataAccessor");
        ClassJavadoc javadoc = null;
        if (!Objects.isNull(metadataAccessor)) {
            List<AnnotatedElement> annotatedElements = (List<AnnotatedElement>) readValue(metadataAccessor, "annotatedElements");
            if (!CollectionUtils.isEmpty(annotatedElements)) {
                AnnotatedElement aClass = annotatedElements.get(0);
                if (!Objects.isNull(aClass)) {
                    Class clazz = (Class) readValue(aClass, "clazz");
                    javadoc = getOrCreate(clazz);
                }
            }
        }
        String name = isNullOrEmpty(context.getParentName())
                ? context.getFieldName()
                : String.format("%s.%s", context.getParentName(), context.getFieldName());
    
        String typeName = context.getDataTypeName();
        ModelReference itemModel = null;
        ResolvedType resolved = resolver.resolve(context.getFieldType());
        if (isContainerType(resolved)) {
            resolved = fieldType(context).or(resolved);
            ResolvedType elementType = collectionElementType(resolved);
            String itemTypeName = typeNameFor(elementType.getErasedType());
            AllowableValues itemAllowables = null;
            if (enumTypeDeterminer.isEnum(elementType.getErasedType())) {
                itemAllowables = Enums.allowableValues(elementType.getErasedType());
                itemTypeName = "string";
            }
            typeName = containerType(resolved);
            itemModel = new ModelRef(itemTypeName, itemAllowables);
        } else if (enumTypeDeterminer.isEnum(resolved.getErasedType())) {
            typeName = "string";
        }
        String description = name;
        boolean required = false;
        String defaultValue = "";
        if (!Objects.isNull(javadoc)) {
            for (FieldJavadoc fieldJavadoc : javadoc.getFields()) {
                if (name.equals(fieldJavadoc.getName())) {
                    String string = fieldJavadoc.getComment().toString();
                    if (StringUtils.isBlank(string)) {
                        break;
                    }
                    String[] split = string.split("\\s+");
                    description = split[0];
                    for (int i = 1; i < split.length; i++) {
                        if ("required".equals(split[i])) {
                            required = true;
                        }
                        if (split[i].startsWith("e_")) {
                            defaultValue = split[i].replace("e_", "");
                        }
                    }
                    break;
                }
            }
        }
        context.getParameterBuilder()
                .name(name)
                .description(description)
                .defaultValue(defaultValue)
                .required(required)
                .allowMultiple(isContainerType(resolved))
                .type(resolved)
                .modelRef(new ModelRef(typeName, itemModel))
                .allowableValues(allowable)
                .parameterType(context.getParameterType())
                .order(DEFAULT_PRECEDENCE)
                .parameterAccess(null);
    }
    
    private Optional<ResolvedType> fieldType(ParameterExpansionContext context) {
        return Optional.of(context.getFieldType());
    }
    
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
    
    private AllowableValues allowableValues(Class<?> fieldType) {
        
        AllowableValues allowable = null;
        if (enumTypeDeterminer.isEnum(fieldType)) {
            allowable = new AllowableListValues(getEnumValues(fieldType), "LIST");
        }
        
        return allowable;
    }
    
    private List<String> getEnumValues(final Class<?> subject) {
        return transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() {
            @Override
            public String apply(final Object input) {
                return input.toString();
            }
        });
    }
}
