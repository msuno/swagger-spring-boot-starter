package cn.msuno.swagger.spring.boot.autoconfigure.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class SwaggerBeanManager implements SmartLifecycle, ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    private DefaultListableBeanFactory defaultListableBeanFactory() {
        return (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
    }
    
    @Override
    public void start() {
        DefaultListableBeanFactory beanFactory = defaultListableBeanFactory();
        beanFactory.removeBeanDefinition("swaggerApiListingReader");
        beanFactory.removeBeanDefinition("apiListingReader");
        beanFactory.removeBeanDefinition("operationNotesReader");
        beanFactory.removeBeanDefinition("operationSummaryReader");
        beanFactory.removeBeanDefinition("operationTagsReader");
        beanFactory.removeBeanDefinition("swaggerOperationTagsReader");
        beanFactory.removeBeanDefinition("operationDeprecatedReader");
        beanFactory.removeBeanDefinition("swaggerParameterDescriptionReader");
        beanFactory.removeBeanDefinition("parameterNameReader");
        beanFactory.removeBeanDefinition("expandedParameterBuilder");
    }
    
    @Override
    public void stop() {
    
    }
    
    @Override
    public boolean isRunning() {
        return false;
    }
    
    @Override
    public boolean isAutoStartup() {
        return true;
    }
    
    @Override
    public void stop(Runnable callback) {
        callback.run();
    }
    
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE -  100;
    }
}
