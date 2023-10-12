package cn.kizzzy.config;

import cn.kizzzy.helper.FileHelper;
import cn.kizzzy.helper.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class Config extends Properties {
    
    private static final Logger logger = LoggerFactory.getLogger(Class.class);
    
    private final boolean autoSave;
    private File file;
    private final Set<Object> keys
        = new TreeSet<>();
    
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
            logger.info("config[{}] is not found", key);
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
            if (FileHelper.createFileIfAbsent(file)) {
                load(Files.newInputStream(file.toPath()));
            }
        } catch (IOException e) {
            file = null;
            logger.error("load config from file failed", e);
        }
    }
    
    public void saveToFile() {
        if (file != null) {
            try {
                store(Files.newOutputStream(file.toPath()), null);
            } catch (IOException e) {
                logger.error("save config to file failed", e);
            }
        }
    }
    
    public File getFile() {
        return file;
    }
}
