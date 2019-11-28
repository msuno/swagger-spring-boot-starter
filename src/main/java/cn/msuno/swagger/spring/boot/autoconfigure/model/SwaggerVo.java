package cn.msuno.swagger.spring.boot.autoconfigure.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import io.swagger.models.Swagger;

public class SwaggerVo extends Swagger {
    
    public SwaggerVo(Swagger swagger) {
        BeanUtils.copyProperties(swagger, this);
    }
    
    private Map<String, String> statusCode;
    
    private List<CustomPage> customPage;
    
    private List<CustomDef> customDef;
    
    public Map<String, String> getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Map<String, String> statusCode) {
        this.statusCode = statusCode;
    }
    
    public List<CustomPage> getCustomPage() {
        return customPage;
    }
    
    public void setCustomPage(List<CustomPage> customPage) {
        this.customPage = customPage;
    }
    
    public List<CustomDef> getCustomDef() {
        return customDef;
    }
    
    public void setCustomDef(List<CustomDef> customDef) {
        this.customDef = customDef;
    }
}
