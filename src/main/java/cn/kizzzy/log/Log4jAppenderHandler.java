package cn.kizzzy.log;

import org.apache.log4j.spi.LoggingEvent;

public interface Log4jAppenderHandler {
    
    void handleLog(LoggingEvent event);
}
