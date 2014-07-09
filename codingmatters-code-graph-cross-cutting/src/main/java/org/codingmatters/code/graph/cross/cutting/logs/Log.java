package org.codingmatters.code.graph.cross.cutting.logs;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 09/07/14
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public class Log {

    static public Log log(Class clazz) {
        return log(clazz.getName());
    }
    
    static public Log log(String name) {
        return new Log(Logger.getLogger(name));
    }
    
        
    
    
    public Log report(Throwable t) {
        this.reported = t;
        return this;
    }
    
    public void error(String messageFormat, Object... args) {
        this.log(new LogRecord(Level.SEVERE, String.format(messageFormat, args)));
    }
    public void warn(String messageFormat, Object ... args) {
        this.log(new LogRecord(Level.WARNING, String.format(messageFormat, args)));
    }
    public void info(String messageFormat, Object ... args) {
        this.log(new LogRecord(Level.INFO, String.format(messageFormat, args)));
    }
    public void debug(String messageFormat, Object... args) {
        this.log(new LogRecord(Level.CONFIG, String.format(messageFormat, args)));
    }
    public void trace(String messageFormat, Object... args) {
        this.log(new LogRecord(Level.FINE, String.format(messageFormat, args)));
    }
    public void finer(String messageFormat, Object ... args) {
        this.log(new LogRecord(Level.FINER, String.format(messageFormat, args)));
    }
    public void finest(String messageFormat, Object ... args) {
        this.log(new LogRecord(Level.FINEST, String.format(messageFormat, args)));
    }

    private final Logger delegate;
    private Throwable reported;

    private Log(Logger delegate) {
        this.delegate = delegate;
    }
    
    private void log(LogRecord record) {
        record.setLoggerName(this.delegate.getName());
        record.setThrown(this.reported);
        this.delegate.log(record);
    }
}
