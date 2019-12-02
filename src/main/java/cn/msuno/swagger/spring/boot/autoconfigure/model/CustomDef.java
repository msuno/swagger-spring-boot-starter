package cn.msuno.swagger.spring.boot.autoconfigure.model;

import java.util.List;

public class CustomDef {
    
    private String title;
    
    private List<Item> def;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<Item> getDef() {
        return def;
    }
    
    public void setDef(List<Item> def) {
        this.def = def;
    }
}
