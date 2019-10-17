package cn.msuno.swagger.spring.boot.autoconfigure.plugins;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.RuntimeJavadoc;


public class SwaggerBuilderPlugin {
    
    protected static Map<Class, ClassJavadoc> javadocMap = Maps.newHashMap();
    
    protected Object readValue(Object obj, String name) {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (name.equals(field.getName())) {
                try {
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static ClassJavadoc getOrCreate(Class clz) {
        if (javadocMap.containsKey(clz)) {
            return javadocMap.get(clz);
        }
        ClassJavadoc javadoc = RuntimeJavadoc.getJavadoc(clz);
        javadocMap.put(clz, javadoc);
        return javadoc;
    }
}
