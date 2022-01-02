package cn.kizzzy.config;

import cn.kizzzy.helper.LogHelper;
import cn.kizzzy.helper.StringHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class Config extends Properties {
    private final boolean autoSave;
    private File file;
    private Set<Object> keys = new TreeSet<>();
    
    public Config() {
        this(true);
    }
    
    public Config(boolean autoSave) {
        this.autoSave = autoSave;
        for (String key : initKeys()) {
            getProperty(key);
        }
    }
    
    protected String[] initKeys() {
        return new String[]{};
    }
    
    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }
    
    @Override
    public String getProperty(String key) {
        return getProperty(key, "");
    }
    
    @Override
    public String getProperty(String key, String defaultValue) {
        if (!containsKey(key)) {
            put(key, defaultValue);
        } else {
            String value = super.getProperty(key);
            if (StringHelper.isNullOrEmpty(value)) {
                put(key, defaultValue);
            }
        }
        return super.getProperty(key);
    }
    
    @Override
    public Object put(Object key, Object value) {
        keys.add(key);
        Object old = super.put(key, value);
        if (autoSave) {
            saveToFile();
        }
        return old;
    }
    
    public boolean checkValid(String key) {
        if (StringHelper.isNullOrEmpty(getProperty(key))) {
            LogHelper.error("[{}] is not config", key);
            return false;
        }
        return true;
    }
    
    public boolean checkValid(String... keys) {
        for (String key : keys) {
            if (!checkValid(key)) {
                return false;
            }
        }
        return true;
    }
    
    public File retrievePath(String key) {
        File file = new File(getProperty(key, "/"));
        if (!file.exists()) {
            file = new File("/");
        }
        return file;
    }
    
    public void loadFromFile(String path) {
        try {
            file = new File(String.format("%s/%s", System.getProperty("user.home"), path));
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    if (!parent.mkdirs()) {
                        throw new IOException("create new file failed: " + path);
                    }
                }
                if (!file.createNewFile()) {
                    throw new IOException("create new file failed: " + path);
                }
            }
            load(new FileInputStream(file));
        } catch (IOException e) {
            file = null;
            LogHelper.error(null, e);
        }
    }
    
    public void saveToFile() {
        if (file != null) {
            try {
                store(new FileOutputStream(file), null);
            } catch (IOException e) {
                LogHelper.error(null, e);
            }
        }
    }
    
    public File getFile() {
        return file;
    }
}
