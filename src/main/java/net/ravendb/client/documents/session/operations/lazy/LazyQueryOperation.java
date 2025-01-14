package net.ravendb.client.documents.session.operations.lazy;

import net.ravendb.client.documents.commands.multiGet.GetRequest;
import net.ravendb.client.documents.commands.multiGet.GetResponse;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.queries.QueryResult;
import net.ravendb.client.documents.session.InMemoryDocumentSessionOperations;
import net.ravendb.client.documents.session.operations.QueryOperation;
import net.ravendb.client.extensions.JsonExtensions;
import net.ravendb.client.primitives.EventHelper;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class LazyQueryOperation<T> implements ILazyOperation {

    private final Class<T> _clazz;
    private final InMemoryDocumentSessionOperations _session;
    private final QueryOperation _queryOperation;
    private final List<Consumer<QueryResult>> _afterQueryExecuted;

    public LazyQueryOperation(Class<T> clazz, InMemoryDocumentSessionOperations session, QueryOperation queryOperation, List<Consumer<QueryResult>> afterQueryExecuted) {
        _clazz = clazz;
        _session = session;
        _queryOperation = queryOperation;
        _afterQueryExecuted = afterQueryExecuted;
    }

    @Override
    public GetRequest createRequest() {
        GetRequest request = new GetRequest();
        request.setCanCacheAggressively(!_queryOperation.getIndexQuery().isDisableCaching() && !_queryOperation.getIndexQuery().isWaitForNonStaleResults());
        request.setUrl("/queries");
        request.setMethod("POST");
        request.setQuery("?queryHash=" + _queryOperation.getIndexQuery().getQueryHash(_session.getConventions().getEntityMapper()));
        request.setContent(new IndexQueryContent(_session.getConventions(), _queryOperation.getIndexQuery()));
        return request;
    }

    private Object result;
    private QueryResult queryResult;
    private boolean requiresRetry;

    @Override
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public QueryResult getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    public boolean isRequiresRetry() {
        return requiresRetry;
    }

    public void setRequiresRetry(boolean requiresRetry) {
        this.requiresRetry = requiresRetry;
    }

    @Override
    public void handleResponse(GetResponse response) {
        if (response.isForceRetry()) {
            result = null;
            requiresRetry = true;
            return;
        }

        QueryResult queryResult = null;

        if (response.getResult() != null) {
            try {
                queryResult = JsonExtensions.getDefaultMapper().readValue(response.getResult(), QueryResult.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        handleResponse(queryResult, response.getElapsed());
    }

    private void handleResponse(QueryResult queryResult, Duration duration) {
        _queryOperation.ensureIsAcceptableAndSaveResult(queryResult, duration);

        EventHelper.invoke(_afterQueryExecuted, queryResult);
        result = _queryOperation.complete(_clazz);
        this.queryResult = queryResult;
    }
}
