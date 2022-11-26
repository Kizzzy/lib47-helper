package cn.kizzzy.base;

public class AttributeWithClass<Attr, Clazz> {
    
    public final Attr attr;
    
    public final Class<? extends Clazz> clazz;
    
    public AttributeWithClass(Attr attr, Class<? extends Clazz> clazz) {
        this.attr = attr;
        this.clazz = clazz;
    }
}
