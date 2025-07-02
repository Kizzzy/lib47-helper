package cn.kizzzy.format;

public interface IField {
    
    boolean ignore();
    
    boolean expand();
    
    String name();
    
    Object value() throws Exception;
}
