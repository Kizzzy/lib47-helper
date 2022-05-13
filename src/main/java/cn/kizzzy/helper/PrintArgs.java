package cn.kizzzy.helper;

public class PrintArgs {
    
    public static class Item {
        public String name;
        public boolean ignore;
        
        public Item(String name, boolean ignore) {
            this.name = name;
            this.ignore = ignore;
        }
    }
    
    public Class<?> clazz;
    public Item[] items;
    public boolean expand;
    
    public PrintArgs(Class<?> clazz, Item[] items, boolean expand) {
        this.clazz = clazz;
        this.items = items;
        this.expand = expand;
    }
}
    
