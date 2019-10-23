package cn.msuno.swagger.spring.boot.autoconfigure.properties;


import java.io.IOException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.annotations.VisibleForTesting;

import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.Xml;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;

public class Swagger2JacksonModule extends SimpleModule implements JacksonModuleRegistrar {
    
    public void maybeRegisterModule(ObjectMapper objectMapper) {
        if (objectMapper.findMixInClassFor(Swagger.class) == null) {
            objectMapper.registerModule(this);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    }
    
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(Swagger.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Info.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                License.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Scheme.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                SecurityRequirement.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                SecuritySchemeDefinition.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Model.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Operation.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Path.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Response.class, Swagger2JacksonModule.ResponseSerializer.class);
        context.setMixInAnnotations(
                Parameter.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                ExternalDocs.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Xml.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Tag.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        context.setMixInAnnotations(
                Contact.class, Swagger2JacksonModule.CustomizedSwaggerSerializer.class);
        
        context.setMixInAnnotations(
                Property.class, Swagger2JacksonModule.PropertyExampleSerializerMixin.class);
    }
    
    @JsonAutoDetect
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private class CustomizedSwaggerSerializer {
    }
    
    @JsonAutoDetect
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties("responseSchema")
    private class ResponseSerializer {
    }
    
    @JsonAutoDetect
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private interface PropertyExampleSerializerMixin {
        
        @JsonSerialize(using = Swagger2JacksonModule.PropertyExampleSerializerMixin.PropertyExampleSerializer.class)
        Object getExample();
        
        class PropertyExampleSerializer extends StdSerializer<Object> {
            
            private final static Pattern JSON_NUMBER_PATTERN =
                    Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");
            
            @SuppressWarnings("unused")
            public PropertyExampleSerializer() {
                this(Object.class);
            }
            
            PropertyExampleSerializer(Class<Object> t) {
                super(t);
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                if (canConvertToString(value)) {
                    String stringValue = (value instanceof String) ? ((String) value).trim() : value.toString().trim();
                    if (isStringLiteral(stringValue)) {
                        String cleanedUp = stringValue.replaceAll("^\"", "")
                                .replaceAll("\"$", "")
                                .replaceAll("^'", "")
                                .replaceAll("'$", "");
                        gen.writeString(cleanedUp);
                    } else if (isNotJsonString(stringValue)) {
                        gen.writeRawValue(stringValue);
                    } else {
                        gen.writeString(stringValue);
                    }
                } else {
                    gen.writeObject(value);
                }
            }
            
            private boolean canConvertToString(Object value) {
                if (value instanceof java.lang.Boolean
                        || value instanceof java.lang.Character
                        || value instanceof java.lang.String
                        || value instanceof java.lang.Byte
                        || value instanceof java.lang.Short
                        || value instanceof java.lang.Integer
                        || value instanceof java.lang.Long
                        || value instanceof java.lang.Float
                        || value instanceof java.lang.Double
                        || value instanceof java.lang.Void) {
                    return true;
                }
                return false;
            }
            
            @VisibleForTesting
            boolean isStringLiteral(String value) {
                return (value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'"));
            }
            
            @VisibleForTesting
            boolean isNotJsonString(final String value) {
                // strictly speaking, should also test for equals("null") since {"example": null} would be valid JSON
                // but swagger2 does not support null values
                // and an example value of "null" probably does not make much sense anyway
                return value.startsWith("{")                          // object
                        || value.startsWith("[")                          // array
                        || "true".equals(value)                           // true
                        || "false".equals(value)                          // false
                        || JSON_NUMBER_PATTERN.matcher(value).matches();  // number
            }
            
            @Override
            public boolean isEmpty(SerializerProvider provider, Object value) {
                return internalIsEmpty(value);
            }
            
            @SuppressWarnings("deprecation")
            @Override
            public boolean isEmpty(Object value) {
                return internalIsEmpty(value);
            }
            
            private boolean internalIsEmpty(Object value) {
                return value == null || value.toString().trim().length() == 0;
            }
        }
    }
    
}
