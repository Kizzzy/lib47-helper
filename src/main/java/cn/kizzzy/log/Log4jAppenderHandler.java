package cn.kizzzy.log;

import org.apache.logging.log4j.core.LogEvent;

public interface Log4jAppenderHandler {
    
    void handleLog(LogEvent event);
}
