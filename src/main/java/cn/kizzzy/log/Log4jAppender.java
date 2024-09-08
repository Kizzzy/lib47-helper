package cn.kizzzy.log;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(name = "Log4jAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class Log4jAppender extends AbstractAppender {
    
    private static final Map<Integer, Log4jAppenderHandler> handlerKvs
        = new ConcurrentHashMap<>();
    
    public Log4jAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
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
     * Format and then append the event to the stored TextArea.
     */
    @Override
    public void append(final LogEvent event) {
        if (!handlerKvs.isEmpty()) {
            for (Log4jAppenderHandler handler : handlerKvs.values()) {
                handler.handleLog(event);
            }
        }
    }
    
    @PluginFactory
    public static Log4jAppender createAppender(
        @PluginAttribute("name") final String name,
        @PluginElement("Filter") final Filter filter,
        @PluginElement("Filter") Layout<? extends Serializable> layout) {
        if (name == null) {
            LOGGER.error("No name provided for Log4jAppender");
            return null;
        } else {
            if (layout == null) {
                layout = PatternLayout.createDefaultLayout();
            }
            
            return new Log4jAppender(name, filter, layout);
        }
    }
}
