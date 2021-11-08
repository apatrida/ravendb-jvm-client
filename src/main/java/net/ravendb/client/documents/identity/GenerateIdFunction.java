package net.ravendb.client.documents.identity;

@FunctionalInterface
public interface GenerateIdFunction {
    String apply(String collectionName, Object entity);
}
