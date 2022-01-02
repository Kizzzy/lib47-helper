package cn.kizzzy.clazz;

public interface ClassFilter {

    String packageRoot();

    boolean isRecursive();

    boolean accept(Class<?> clazz);
}
