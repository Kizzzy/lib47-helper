package cn.kizzzy.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    
    private static final float KB = 1024;
    private static final float MB = 1024 * KB;
    private static final float GB = 1024 * MB;
    
    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long size) {
        if (size >= GB) {
            return String.format("%.1f GB", size / GB);
        } else if (size >= MB) {
            float value = size / MB;
            return String.format(value > 100 ? "%.0f MB" : "%.1f MB", value);
        } else if (size >= KB) {
            float value = size / KB;
            return String.format(value > 100 ? "%.0f KB" : "%.1f KB", value);
        } else {
            return String.format("%d B", size);
        }
    }
    
    /**
     * 检测文件是否存在，不存在则创建
     */
    public static boolean createFileIfAbsent(String path) throws IOException {
        return createFileIfAbsent(new File(path));
    }
    
    /**
     * 检测文件是否存在，不存在则创建
     */
    public static boolean createFileIfAbsent(File file) throws IOException {
        if (!file.exists()) {
            if (!createFolderIfAbsent(file.getParentFile())) {
                return false;
            }
            
            if (!file.createNewFile()) {
                throw new IOException("创建文件失败: " + file.getCanonicalPath());
            }
        }
        return true;
    }
    
    /**
     * 检测文件夹是否存在，不存在则创建
     */
    public static boolean createFolderIfAbsent(String path) throws IOException {
        return createFolderIfAbsent(new File(path));
    }
    
    /**
     * 检测文件夹是否存在，不存在则创建
     */
    public static boolean createFolderIfAbsent(File file) throws IOException {
        if (!file.exists() && !file.mkdirs()) {
            throw new IOException("文件夹创建失败: " + file.getCanonicalPath());
        }
        return true;
    }
    
    private static File[] filterFiles(String path, FileFilter filter) {
        if (path == null) {
            return null;
        }
        return new File(path).listFiles(filter);
    }
    
    public static List<File> listFolder(String path) throws FileNotFoundException {
        return listFolder(new File(path), null);
    }
    
    public static List<File> listFolder(File file) throws FileNotFoundException {
        return listFolder(file, null);
    }
    
    public static List<File> listFolder(String path, FileFilter filter) throws FileNotFoundException {
        return listFolder(new File(path), filter);
    }
    
    public static List<File> listFolder(File file, FileFilter filter) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("folder not found, " + file);
        }
        
        List<File> list = new ArrayList<>();
        listFolder0(list, file, filter);
        return list;
    }
    
    private static void listFolder0(List<File> list, File file, FileFilter filter) {
        File[] files = file.listFiles(filter);
        if (files != null) {
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
    
    
    public static byte[] readBytes(String path) throws IOException {
        return readBytes(new File(path));
    }
    
    public static byte[] readBytes(File file) throws IOException {
        byte[] buffer = new byte[8192];
        try (FileInputStream in = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (int len; (len = in.read(buffer)) != -1; ) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }
    
    public static void writeBytes(String path, byte[] data) throws IOException {
        writeBytes(new File(path), data);
    }
    
    public static void writeBytes(File file, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }
    
    /**
     * 读取指定路径的文件到文本
     */
    public static String readText(String path) throws IOException {
        return readText(new File(path));
    }
    
    /**
     * 读取指定路径的文件到文本
     */
    public static String readText(File file) throws IOException {
        return readText(file, StandardCharsets.UTF_8);
    }
    
    /**
     * 读取指定路径的文件到文本
     */
    public static String readText(String path, Charset charset) throws IOException {
        return readText(new File(path), charset);
    }
    
    /**
     * 读取指定路径的文件到文本
     */
    public static String readText(File file, Charset charset) throws IOException {
        return new String(readBytes(file), charset);
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeText(String path, String content) throws IOException {
        writeText(new File(path), content);
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeText(File file, String content) throws IOException {
        writeText(file, content, StandardCharsets.UTF_8);
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeText(String path, String content, Charset charset) throws IOException {
        writeText(new File(path), content, charset);
    }
    
    /**
     * 将文本写入到指定路径的文件
     */
    public static void writeText(File file, String content, Charset charset) throws IOException {
        writeBytes(file, content.getBytes(charset));
    }
    
    public static <T> T readObject(String path, Class<T> clazz) throws IOException, ClassNotFoundException {
        return readObject(new File(path), clazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T readObject(File file, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        }
    }
    
    public static void writeObject(String path, Object obj) throws IOException {
        writeObject(new File(path), obj);
    }
    
    public static void writeObject(File file, Object obj) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        }
    }
}
