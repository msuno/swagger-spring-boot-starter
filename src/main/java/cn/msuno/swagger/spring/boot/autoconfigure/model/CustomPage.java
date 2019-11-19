package cn.msuno.swagger.spring.boot.autoconfigure.model;

public class CustomPage {
    
    /**
     * 菜单
     */
    private String tag;
    
    /**
     * markdown内容
     */
    private String content;
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
