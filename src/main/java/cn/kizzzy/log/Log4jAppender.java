package cn.kizzzy.log;


import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(
    name = "Custom",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public class Log4jAppender extends AbstractAppender {
    
    public static final String PLUGIN_NAME = "Custom";
    
    private static final Map<Integer, Log4jAppenderHandler> handlerKvs
        = new ConcurrentHashMap<>();
    
    private final String customProperty;
    
    @PluginFactory
    public static Log4jAppender createAppender(
        @PluginAttribute("name") String name,
        @PluginAttribute("ignoreExceptions") Boolean ignoreExceptions,
        @PluginElement("Layout") Layout<? extends Serializable> layout,
        @PluginElement("Filter") Filter filter,
        @PluginAttribute("customProperty") String customProperty) {
        
        if (name == null) {
            LOGGER.error("No name provided for Log4j2Appender");
            return null;
        }
        
        if (layout == null) {
            try {
                layout = PatternLayout.createDefaultLayout();
            } catch (Exception e) {
                LOGGER.error("Failed to create default layout, using simple layout", e);
                layout = PatternLayout.newBuilder().withPattern("[%-5p] %d %c - %m%n").build();
            }
        }
        
        if (layout == null) {
            LOGGER.error("Still no layout, cannot create Appender");
            return null;
        }
        
        boolean ignoreExcepts = ignoreExceptions == null ? true : ignoreExceptions;
        
        return new Log4jAppender(name, filter, layout, ignoreExcepts, customProperty);
    }
    
    protected Log4jAppender(
        String name,
        Filter filter,
        Layout<? extends Serializable> layout,
        boolean ignoreExceptions,
        String customProperty) {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.customProperty = customProperty;
    }
    
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
    public void append(LogEvent event) {
        if (!handlerKvs.isEmpty()) {
            for (Log4jAppenderHandler handler : handlerKvs.values()) {
                handler.handleLog(event);
            }
        }
    }
}
