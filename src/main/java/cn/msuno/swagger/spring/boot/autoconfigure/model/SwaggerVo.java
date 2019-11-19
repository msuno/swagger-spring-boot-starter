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
}
