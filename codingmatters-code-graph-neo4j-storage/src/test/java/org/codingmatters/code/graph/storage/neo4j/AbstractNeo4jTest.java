package org.codingmatters.code.graph.storage.neo4j;

import org.junit.After;
import org.junit.Before;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 25/06/14
 * Time: 00:44
 * To change this template use File | Settings | File Templates.
 */
public class AbstractNeo4jTest {
    private GraphDatabaseService graphDb;
    private ExecutionEngine engine;
    
    @Before
    public void setUpGraphDb() throws Exception {
        this.graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        this.engine = new ExecutionEngine(this.getGraphDb(), StringLogger.SYSTEM);
    }

    @After
    public void tearDown() throws Exception {
        this.graphDb.shutdown();
    }
    
    protected GraphDatabaseService getGraphDb() {
        return this.graphDb;
    }


    public ExecutionEngine getEngine() {
        return engine;
    }
}
