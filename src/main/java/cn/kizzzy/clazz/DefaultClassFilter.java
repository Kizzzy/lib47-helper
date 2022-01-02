package cn.kizzzy.clazz;

public class DefaultClassFilter implements ClassFilter {

    @Override
    public String packageRoot() {
        return ".";
    }

    @Override
    public boolean isRecursive() {
        return true;
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return true;
    }
}
