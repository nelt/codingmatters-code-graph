package org.codingmatters.code.graph.main;

import org.codingmatters.code.graph.api.producer.NodeProducer;
import org.codingmatters.code.graph.api.producer.PredicateProducer;
import org.codingmatters.code.graph.bytecode.parser.JarParser;
import org.codingmatters.code.graph.bytecode.parser.exception.ClassParserException;
import org.codingmatters.code.graph.cross.cutting.logs.Log;
import org.codingmatters.code.graph.storage.neo4j.Neo4jNodeProducer;
import org.codingmatters.code.graph.storage.neo4j.Neo4jPredicateProducer;
import org.codingmatters.code.graph.storage.neo4j.Neo4jStore;
import org.codingmatters.code.graph.storage.neo4j.postprocess.OverridesPostProcessor;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 06/07/14
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */
public class CodeGraphRunner {
    
    static private final Log log = Log.get(CodeGraphRunner.class);
    
    public static void main(String[] args) {
        if(args.length < 1) throw new RuntimeException("usage : <db path> {<jar path>...}");
        
        String source = "TEST";
        String dbPath = args[0];
        
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
        registerShutdownHook(graphDb);
        
        log.info("neo4j graph is up");
        
        Neo4jStore.initializer(graphDb).run();

        ExecutionEngine engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);
        NodeProducer nodeProducer = new Neo4jNodeProducer(graphDb, engine);
        PredicateProducer predicateProducer = new Neo4jPredicateProducer(graphDb, engine);

        log.info("will parse %s jar files...", args.length - 1);
        for(int i = 1 ; i < args.length ; i++) {
            try {
                log.info("parsing %s...", args[i]);
                try(Transaction tx = graphDb.beginTx();) {
                    JarParser.parse(new File(args[i]), nodeProducer, predicateProducer, source);
                    tx.success();
                }
                log.info("done parsing %s.", args[i]);
            } catch (ClassParserException e) {
                log.report(e).error("error parsing jar file " + args[i]);
            }
        }

        log.info("postprocessing source %s", source);
        new OverridesPostProcessor(graphDb, engine).process(source);
        
        log.info("done.");
        log.info("db files are stored in " + dbPath);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
}
