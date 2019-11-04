package cn.msuno.swagger.spring.boot.autoconfigure.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class SwaggerResourcesConfigProvider implements SwaggerResourcesProvider {
    private final String swaggerUrl;
    @VisibleForTesting
    boolean swaggerAvailable;
    
    private final DocumentationCache documentationCache;
    
    @Autowired
    public SwaggerResourcesConfigProvider(
            Environment environment,
            DocumentationCache documentationCache) {
        swaggerUrl = environment.getProperty("swagger.api.url", "/v2/api-docs");
        swaggerAvailable = true;
        this.documentationCache = documentationCache;
    }
    
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
        
        for (Map.Entry<String, Documentation> entry : documentationCache.all().entrySet()) {
            String swaggerGroup = entry.getKey();
            if (swaggerAvailable) {
                SwaggerResource swaggerResource = resource(swaggerGroup, swaggerUrl);
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            }
        }
        Collections.sort(resources);
        return resources;
    }
    
    private SwaggerResource resource(String swaggerGroup, String baseUrl) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(swaggerGroup);
        swaggerResource.setUrl(swaggerLocation(baseUrl, swaggerGroup));
        return swaggerResource;
    }
    
    private String swaggerLocation(String swaggerUrl, String swaggerGroup) {
        String base = Optional.of(swaggerUrl).get();
        if (Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
            return base;
        }
        return base + "?group=" + swaggerGroup;
    }
}
