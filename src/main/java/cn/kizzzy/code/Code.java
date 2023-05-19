package cn.kizzzy.code;

public class Code implements ICode {
    
    private final int shift;
    
    private final int code;
    
    private final String message;
    
    private static final ICode ROOT = new ICode() {
        @Override
        public int shift() {
            return 0;
        }
        
        @Override
        public int code() {
            return 0;
        }
        
        @Override
        public String message() {
            return "";
        }
    };
    
    public Code(int shift, int code, String message) {
        this(ROOT, shift, code, message);
    }
    
    public Code(ICode parent, int shift, int code, String message) {
        this.shift = shift;
        this.code = (parent.code() << parent.shift()) | code;
        this.message = String.format("%s -> %s[0x%02X(%d)]", parent.message(), message, code(), code());
    }
    
    @Override
    public int shift() {
        return shift;
    }
    
    @Override
    public int code() {
        return code;
    }
    
    @Override
    public String message() {
        return message;
    }
}
