package net.ravendb.client.documents.operations.counters;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.List;

public class DocumentCountersOperation {

    private List<CounterOperation> operations;
    private String documentId;

    public void serialize(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("DocumentId", documentId);

        generator.writeFieldName("Operations");
        generator.writeStartArray();
        for (CounterOperation operation : operations) {
            operation.serialize(generator);
        }
        generator.writeEndArray();

        generator.writeEndObject();
    }

    public List<CounterOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<CounterOperation> operations) {
        this.operations = operations;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
