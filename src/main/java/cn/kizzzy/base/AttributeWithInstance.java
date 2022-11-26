package cn.kizzzy.base;

public class AttributeWithInstance<Attr, Instance> {
    
    public final Attr attr;
    
    public final Instance instance;
    
    public AttributeWithInstance(Attr attr, Instance instance) {
        this.attr = attr;
        this.instance = instance;
    }
}
