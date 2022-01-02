package cn.kizzzy.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    
    /**
     * 检测文件是否存在，不存在则创建
     */
    public static boolean createFileIfAbsent(String path) {
        return createFileIfAbsent(new File(path));
    }
    
    /**
     * 检测文件是否存在，不存在则创建
     */
    public static boolean createFileIfAbsent(File file) {
        try {
            if (!file.exists()) {
                if (!createFolderIfAbsent(file.getParentFile())) {
                    return false;
                }
                if (!file.createNewFile()) {
                    throw new IOException("创建文件失败: " + file.getCanonicalPath());
                }
            }
            return true;
        } catch (IOException e) {
            LogHelper.error(null, e);
        }
        return false;
    }
    
    /**
     * 检测文件夹是否存在，不存在则创建
     */
    public static boolean createFolderIfAbsent(String path) {
        return createFolderIfAbsent(new File(path));
    }
    
    /**
     * 检测文件夹是否存在，不存在则创建
     */
    public static boolean createFolderIfAbsent(File file) {
        try {
            if (!file.exists() && !file.mkdirs()) {
                throw new IOException("文件夹创建失败: " + file.getCanonicalPath());
            }
            return true;
        } catch (IOException e) {
            LogHelper.error(null, e);
        }
        return false;
    }
    
    public static String readTextFile(String path, String charset) throws IOException {
        FileInputStream in = new FileInputStream(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        
        int len, off = 0;
        while ((len = in.read(buffer)) != -1) {
            bao.write(buffer, off, len);
        }
        in.close();
        return new String(bao.toByteArray(), charset);
    }
    
    public static byte[] readToString(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        
        int len, off = 0;
        while ((len = in.read(buffer)) != -1) {
            bao.write(buffer, off, len);
        }
        in.close();
        return bao.toByteArray();
    }
    
    /**
     * 读取指定路径的文件到文本
     */
    public static String readTextFromFile(String path) {
        File file = new File(path);
        if (createFileIfAbsent(file)) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] fileContent = new byte[(int) file.length()];
                if (fis.read(fileContent) != -1) {
                    return new String(fileContent);
                }
            } catch (IOException e) {
                LogHelper.error(null, e);
            }
        }
        return null;
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeTextToFile(String path, String s) {
        writeTextToFile(path, s, StandardCharsets.UTF_8);
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeTextToFile(String path, String s, Charset charset) {
        File file = new File(path);
        if (createFileIfAbsent(file)) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fos, charset)) {
                writer.write(s);
                writer.flush();
            } catch (Exception e) {
                LogHelper.error(null, e);
            }
        }
    }
    
    private static File[] filterFiles(String path, FileFilter filter) {
        if (path == null) {
            return null;
        }
        return new File(path).listFiles(filter);
    }
    
    public static List<File> listFolder(String path) {
        return listFolder(new File(path), null);
    }
    
    public static List<File> listFolder(File file) {
        return listFolder(file, null);
    }
    
    public static List<File> listFolder(String path, FileFilter filter) {
        return listFolder(new File(path), filter);
    }
    
    public static List<File> listFolder(File file, FileFilter filter) {
        List<File> list = new ArrayList<>();
        
        if (!file.exists()) {
            LogHelper.info("path({}) is not valid", file.getAbsolutePath());
            return list;
        }
        
        listFolder0(list, file, filter);
        return list;
    }
    
    private static void listFolder0(List<File> list, File file, FileFilter filter) {
        File[] files = file.listFiles(filter);
        if (files != null && files.length > 0) {
            for (File temp : files) {
                if (temp.isFile()) {
                    list.add(temp);
                } else {
                    listFolder0(list, temp, filter);
                }
            }
        }
    }
    
    public static String getName(String path) {
        int sepIndex = path.lastIndexOf("\\");
        return path.substring(sepIndex + 1);
    }
    
    public static String getExtension(String path) {
        int dotIndex = path.lastIndexOf(".");
        return path.substring(dotIndex + 1);
    }
    
    public static String getNameNoExt(String path) {
        int sepIndex = path.lastIndexOf("\\");
        int dotIndex = path.lastIndexOf(".");
        return path.substring(sepIndex + 1, dotIndex);
    }
    
    public static void writeObject(String path, Object obj) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> T readObject(String path, Class<T> clazz) {
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
