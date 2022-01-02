package cn.kizzzy.clazz;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 第三方Jar类库的引用。<br/>
 */
public class JarClassFinder extends BaseClassFinder {
    
    @Override
    public List<Class<?>> find(URL url, ClassFilter filter) throws IOException, ClassNotFoundException {
        List<Class<?>> list = new LinkedList<>();
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry entry = jarEntries.nextElement();
            String path = entry.getName().replace("/", ".");
            if (path.endsWith(".class") && path.startsWith(filter.packageRoot())) {
                path = path.substring(0, path.indexOf(".class"));
                
                if (filter.isRecursive()) {
                    loadClass(list, path, filter);
                } else if (!path.substring(filter.packageRoot().length() + 1).contains(".")) {
                    loadClass(list, path, filter);
                }
            }
        }
        
        return list;
    }
}
