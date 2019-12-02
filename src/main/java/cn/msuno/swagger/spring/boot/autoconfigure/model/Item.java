package cn.msuno.swagger.spring.boot.autoconfigure.model;

public class Item {
    private String value;
    private String type;
    private String desc;
    
    public Item() {}
    
    public Item(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public Item(String value, String type, String desc) {
        this.value = value;
        this.type = type;
        this.desc = desc;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
