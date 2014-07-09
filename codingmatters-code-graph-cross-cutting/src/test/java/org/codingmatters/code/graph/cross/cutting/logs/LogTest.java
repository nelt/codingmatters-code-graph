package org.codingmatters.code.graph.cross.cutting.logs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 09/07/14
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class LogTest {

    static public Log log;

    public static final String LOGGER_NAME = "test.logger";
    private final List<LogRecord> records = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }
            @Override
            public void flush() {}
            @Override
            public void close() throws SecurityException {}
        };
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.FINEST);

        log = Log.log(this.getClass());
    }

    @After
    public void tearDown() throws Exception {
        this.records.clear();
    }

    @Test
    public void testError() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.error("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).error("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.SEVERE, "log without exception");
        this.assertLogged(1, Level.SEVERE, "log with an exception", e);
    }

    @Test
    public void testWarn() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.warn("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).warn("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.WARNING, "log without exception");
        this.assertLogged(1, Level.WARNING, "log with an exception", e);
    }

    @Test
    public void testInfo() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.info("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).info("log %s %s %s", "with", "an", "exception");
        }
        
        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.INFO, "log without exception");
        this.assertLogged(1, Level.INFO, "log with an exception", e);
    }

    @Test
    public void testDebug() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.debug("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).debug("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.CONFIG, "log without exception");
        this.assertLogged(1, Level.CONFIG, "log with an exception", e);
    }
    
    @Test
    public void testTrace() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.trace("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).trace("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.FINE, "log without exception");
        this.assertLogged(1, Level.FINE, "log with an exception", e);
    }

    @Test
    public void testFiner() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.finer("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).finer("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.FINER, "log without exception");
        this.assertLogged(1, Level.FINER, "log with an exception", e);
    }

    @Test
    public void testFinest() throws Exception {
        Exception e = new RuntimeException("boom !");
        log.finest("log %s %s", "without", "exception");
        try {throw e;} catch (Throwable t) {
            log.report(t).finest("log %s %s %s", "with", "an", "exception");
        }

        Assert.assertEquals(2, this.records.size());
        this.assertLogged(0, Level.FINEST, "log without exception");
        this.assertLogged(1, Level.FINEST, "log with an exception", e);
    }
    
    protected void assertLogged(int at, Level expectedLevel, String expectedMessage) {
        this.assertLogged(at, expectedLevel, expectedMessage, null);
    }
    protected void assertLogged(int at, Level expectedLevel, String expectedMessage, Throwable expectedThrowable) {
        LogRecord actual = records.get(at);
        Assert.assertEquals(this.getClass().getName(), actual.getLoggerName());
        Assert.assertEquals(expectedLevel, actual.getLevel());
        Assert.assertEquals(expectedMessage, actual.getMessage());
        Assert.assertEquals(expectedThrowable, actual.getThrown());
    }
}
