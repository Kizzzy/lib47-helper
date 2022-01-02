package cn.kizzzy.log;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomAppender extends WriterAppender {
    private static Map<Integer, CustomAppenderHandler> handlers = new ConcurrentHashMap<>();

    /**
     * add handler
     */
    public static void add(CustomAppenderHandler handler) {
        handlers.put(handler.hashCode(), handler);
    }

    /**
     * remove handler
     */
    public static void remove(CustomAppenderHandler handler) {
        handlers.remove(handler.hashCode());
    }

    /**
     * Format and then append the loggingEvent to the stored TextArea.
     */
    @Override
    public void append(final LoggingEvent loggingEvent) {
        if (handlers.size() > 0) {
            String message = this.layout.format(loggingEvent);
            for (CustomAppenderHandler handler : handlers.values()) {
                handler.handleLog(message);
            }
        }
    }
}
