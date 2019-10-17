package cn.msuno.swagger.spring.boot.autoconfigure.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.msuno.swagger.spring.boot.autoconfigure.SwaggerAutoConfiguration;
import cn.msuno.swagger.spring.boot.autoconfigure.configuration.SwaggerDocumentationConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({Swagger2DocumentationConfiguration.class, SwaggerAutoConfiguration.class, SwaggerDocumentationConfiguration.class})
public @interface EnableJavadocSwagger2 {

}
