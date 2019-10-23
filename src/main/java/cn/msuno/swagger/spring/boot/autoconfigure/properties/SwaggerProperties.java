package cn.msuno.swagger.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置
 *
 * @author msuno
 */
@ConfigurationProperties(prefix = "spring.swagger")
public class SwaggerProperties {
    
    private String basePackage;
    
    private String title = "msuno";
    
    private String description = "swagger restful api doc";
    
    private String termsOfServiceUrl = "http://localhost";
    
    private String version = "1.0.0";
    
    private Class<?> clazz;
    
    public String getBasePackage() {
        return basePackage;
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }
    
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Class<?> getClazz() {
        return clazz;
    }
    
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
