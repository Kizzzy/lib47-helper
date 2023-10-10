package cn.kizzzy.log;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Log4jAppender extends WriterAppender {
    
    private static final Map<Integer, Log4jAppenderHandler> handlerKvs
        = new ConcurrentHashMap<>();
    
    /**
     * add handler
     */
    public static void add(Log4jAppenderHandler handler) {
        handlerKvs.put(handler.hashCode(), handler);
    }
    
    /**
     * remove handler
     */
    public static void remove(Log4jAppenderHandler handler) {
        handlerKvs.remove(handler.hashCode());
    }
    
    /**
     * Format and then append the loggingEvent to the stored TextArea.
     */
    @Override
    public void append(final LoggingEvent loggingEvent) {
        if (!handlerKvs.isEmpty()) {
            String message = this.layout.format(loggingEvent);
            for (Log4jAppenderHandler handler : handlerKvs.values()) {
                handler.handleLog(message);
            }
        }
    }
}
