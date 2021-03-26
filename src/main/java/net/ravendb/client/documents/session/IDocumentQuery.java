package net.ravendb.client.documents.session;

import net.ravendb.client.documents.queries.QueryData;
import net.ravendb.client.documents.queries.QueryResult;

/**
 * A query against a Raven index
 */
public interface IDocumentQuery<T> extends IDocumentQueryBase<T, IDocumentQuery<T>>, IDocumentQueryBaseSingle<T>, IEnumerableQuery<T> {

    String getIndexName();

    Class<T> getQueryClass();

    /**
     * Whether we should apply distinct operation to the query on the server side
     * @return true if server should return distinct results
     */
    boolean isDistinct();

    /**
     * Returns the query result. Accessing this property for the first time will execute the query.
     * @return query result
     */
    QueryResult getQueryResult();

    /**
     * Selects the specified fields directly from the index if the are stored. If the field is not stored in index, value
     * will come from document directly.
     * @param <TProjection> projection class
     * @param projectionClass projection class
     * @return Document query
     */
    <TProjection> IDocumentQuery<TProjection> selectFields(Class<TProjection> projectionClass);

    /**
     * Selects the specified fields directly from the index if the are stored. If the field is not stored in index, value
     * will come from document directly.
     * @param <TProjection> projection class
     * @param projectionClass projection class
     * @param fields Fields to fetch
     * @return Document query
     */
    <TProjection> IDocumentQuery<TProjection> selectFields(Class<TProjection> projectionClass, String... fields);

    /**
     * Selects the specified fields directly from the index if the are stored. If the field is not stored in index, value
     * will come from document directly.
     * @param <TProjection> projection class
     * @param projectionClass projection class
     * @param queryData Query data to use
     * @return Document query
     */
    <TProjection> IDocumentQuery<TProjection> selectFields(Class<TProjection> projectionClass, QueryData queryData);


    /**
     * Changes the return type of the query
     * @param <TResult> class of result
     * @param resultClass class of result
     * @return Document query
     */
    <TResult> IDocumentQuery<TResult> ofType(Class<TResult> resultClass);


}
