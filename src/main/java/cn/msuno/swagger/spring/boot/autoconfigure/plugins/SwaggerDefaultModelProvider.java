package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.FieldJavadoc;
import springfox.documentation.schema.DefaultModelProvider;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelDependencyProvider;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.schema.property.ModelPropertiesProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;

@Component
@Primary
public class SwaggerDefaultModelProvider extends DefaultModelProvider {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    public SwaggerDefaultModelProvider(TypeResolver resolver,
            @Qualifier("cachedModelProperties") ModelPropertiesProvider propertiesProvider,
            @Qualifier("cachedModelDependencies") ModelDependencyProvider dependencyProvider,
            SchemaPluginsManager schemaPluginsManager,
            TypeNameExtractor typeNameExtractor) {
        super(resolver, propertiesProvider, dependencyProvider, schemaPluginsManager, typeNameExtractor);
    }
    
    @Override
    public Optional<Model> modelFor(ModelContext modelContext) {
        Optional<Model> modelOptional = super.modelFor(modelContext);
        ClassJavadoc javadoc = null;
        Model model = null;
        if (modelOptional.isPresent()) {
            model = modelOptional.get();
            try {
                Class<?> aClass = Class.forName(model.getQualifiedType());
                javadoc = SwaggerBuilderPlugin.getOrCreate(aClass);
            } catch (ClassNotFoundException e) {
                logger.info("get class error {}", e.getMessage());
            }
        }
        if (null != javadoc) {
            updateField(model, "description", javadoc.getComment().toString());
            Map<String, ModelProperty> properties = model.getProperties();
            for (Map.Entry<String, ModelProperty> kv : properties.entrySet()) {
                for (FieldJavadoc fieldJavadoc : javadoc.getFields()) {
                    if (fieldJavadoc.getName().equals(kv.getKey())) {
                        updateField(kv.getValue(), "description", fieldJavadoc.getComment().toString());
                    }
                }
            }
        }
        return modelOptional;
    }
    
    @Override
    public Map<String, Model> dependencies(ModelContext modelContext) {
        return super.dependencies(modelContext);
    }
    
    private void updateField(Object obj, String key, String value) {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (key.equals(field.getName())) {
                try {
                    field.set(obj, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
