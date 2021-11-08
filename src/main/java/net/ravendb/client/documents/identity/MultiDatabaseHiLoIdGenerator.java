package net.ravendb.client.documents.identity;

import net.ravendb.client.documents.DocumentStore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MultiDatabaseHiLoIdGenerator {

    protected final DocumentStore store;

    private final ConcurrentMap<String, MultiTypeHiLoIdGenerator> _generators = new ConcurrentHashMap<>();

    public MultiDatabaseHiLoIdGenerator(DocumentStore store) {
        this.store = store;
    }

    public String generateDocumentId(String databaseName, String collectionName, Object entity) {
        databaseName = store.getEffectiveDatabase(databaseName);
        MultiTypeHiLoIdGenerator generator = _generators.computeIfAbsent(databaseName, x -> generateMultiTypeHiLoFunc(x));
        return generator.generateDocumentId(collectionName, entity);
    }

    public MultiTypeHiLoIdGenerator generateMultiTypeHiLoFunc(String database) {
        return new MultiTypeHiLoIdGenerator(store, database);
    }

    public void returnUnusedRange() {
        for (MultiTypeHiLoIdGenerator generator : _generators.values()) {
            generator.returnUnusedRange();
        }
    }

}
