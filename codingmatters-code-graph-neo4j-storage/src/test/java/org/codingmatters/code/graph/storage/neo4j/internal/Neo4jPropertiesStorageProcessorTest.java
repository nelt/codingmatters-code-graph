package org.codingmatters.code.graph.storage.neo4j.internal;

import org.codingmatters.code.graph.storage.neo4j.internal.storable.Data;
import org.codingmatters.code.graph.storage.neo4j.internal.storable.Props;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: nel
 * Date: 12/07/14
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class Neo4jPropertiesStorageProcessorTest {

    private Neo4jPropertiesStorageProcessor processor = new Neo4jPropertiesStorageProcessor();

    @Test
    public void testPrepareStorage() throws Exception {
        Data data = new Data()
                .withStorable(new Props.Builder().withField1("F1").withField2("F2").build())
                .withNotStorable(new Props.Builder().withField1("not storable").build())
                ;
        
        Neo4jPropertiesStorageProcessor.ToStore actual = this.processor.prepareStorage("n", data);

        assertThat(actual.getPropertyMergerString())
                .isEqualTo("n.storable_field1 = {storable_field1}, n.storable_field2 = {storable_field2}");
            
        assertThat(actual.getParameters())
                .isEqualTo(
                    map("storable_field1", "F1")
                    .map("storable_field2", "F2")
                );
    }
    
    
    
    
    static public FluentMap<String, Object> map(String key, Object value) {
        return new FluentMap<String, Object>().map(key, value);
    }
    
    static public class FluentMap<K, V> extends HashMap<K, V> implements Map<K, V> {
        public FluentMap<K, V> map(K key, V value) {
            this.put(key, value);
            return this;
        }
    }
}
