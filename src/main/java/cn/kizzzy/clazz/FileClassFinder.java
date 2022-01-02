package cn.kizzzy.clazz;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FileClassFinder extends BaseClassFinder {
    
    @Override
    public List<Class<?>> find(URL url, ClassFilter filter) throws ClassNotFoundException {
        List<Class<?>> list = new LinkedList<>();
        findClassName(list, url.getPath(), filter.packageRoot(), filter);
        return list;
    }
    
    public void findClassName(List<Class<?>> list, String path, String pkgName, ClassFilter filter) throws ClassNotFoundException {
        File[] files = listFiles(path);
        for (File f : files) {
            String fileName = f.getName();
            if (f.isFile()) {
                loadClass(list, getClassName(pkgName, fileName), filter);
            } else if (filter.isRecursive()) {
                findClassName(list, path + "/" + fileName, pkgName + "." + fileName, filter);
            }
        }
    }
    
    private File[] listFiles(String pkgPath) {
        return new File(pkgPath).listFiles((file) -> {
            return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
        });
    }
    
    private String getClassName(String pkgName, String fileName) {
        int endIndex = fileName.lastIndexOf(".");
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }
}
