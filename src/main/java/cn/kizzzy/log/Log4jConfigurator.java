package cn.kizzzy.log;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;

import java.util.Properties;

public class Log4jConfigurator extends PropertyConfigurator {
    
    private void configRoot(Properties properties, String key, String root) {
        properties.setProperty(key, String.format("%s/%s", root, properties.getProperty(key)));
    }
    
    @Override
    public void doConfigure(Properties properties, LoggerRepository hierarchy) {
        configRoot(properties, "log4j.custom.file.info", System.getProperty("user.home"));
        configRoot(properties, "log4j.custom.file.error", System.getProperty("user.home"));
        super.doConfigure(properties, hierarchy);
    }
}
