package net.ravendb.client.documents.identity;

@FunctionalInterface
public interface DatabaseGenerateIdFunction {
    String apply(String databaseName, String collectionName, Object entity);
}
