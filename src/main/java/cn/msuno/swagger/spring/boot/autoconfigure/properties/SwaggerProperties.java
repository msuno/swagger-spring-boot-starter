package cn.msuno.swagger.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import springfox.documentation.service.Contact;

/**
 * swagger配置
 *
 * @author msuno
 */
@ConfigurationProperties(prefix = "spring.swagger")
public class SwaggerProperties {
    
    private String title = "Restful API";
    private String swagger = "3.0";
    private String host = "http://localhost:8080";
    private String basePath = "/";
    private String description = "RESTFUL API";
    private String version = "3.0";
    private String termsOfService = "https://www.msuno.cn";
    private String basePackage = "cn.msuno";
    private Contact contact = new Contact("msuno", "https://www.msuno.cn", "msuno@msuno.cn");
    private String license = "https://www.msuno.cn";
    private boolean enable = true;
    
    private Class<?> clazz;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSwagger() {
        return swagger;
    }
    
    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getBasePath() {
        return basePath;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getTermsOfService() {
        return termsOfService;
    }
    
    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }
    
    public Contact getContact() {
        return contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public Class<?> getClazz() {
        return clazz;
    }
    
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    public String getBasePackage() {
        return basePackage;
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
