package cn.msuno.swagger.spring.boot.autoconfigure.model;

import java.util.Map;

import org.springframework.beans.BeanUtils;

import io.swagger.models.Swagger;

public class SwaggerVo extends Swagger {
    
    public SwaggerVo(Swagger swagger) {
        BeanUtils.copyProperties(swagger, this);
    }
    
    private Map<String, String> statusCode;
    
    public Map<String, String> getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Map<String, String> statusCode) {
        this.statusCode = statusCode;
    }
}
