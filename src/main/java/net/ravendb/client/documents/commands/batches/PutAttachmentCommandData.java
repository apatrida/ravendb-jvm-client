package net.ravendb.client.documents.commands.batches;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class PutAttachmentCommandData implements ICommandData {

    private String id;
    private String name;
    private InputStream stream;
    private String changeVector;
    private String contentType;
    private CommandType type = CommandType.ATTACHMENT_PUT;


    public PutAttachmentCommandData(String documentId, String name, InputStream stream, String contentType, String changeVector) {
        if (StringUtils.isWhitespace(documentId)) {
            throw new IllegalArgumentException("DocumentId cannot be null");
        }

        if (StringUtils.isWhitespace(name)) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        this.id = documentId;
        this.name = name;
        this.stream = stream;
        this.contentType = contentType;
        this.changeVector = changeVector;

        //TODO: PutAttachmentCommandHelper.ValidateStream(stream);? do we need this in java?
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public InputStream getStream() {
        return stream;
    }

    @Override
    public String getChangeVector() {
        return changeVector;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public CommandType getType() {
        return type;
    }

    @Override
    public void serialize(JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("Id", id);
        generator.writeStringField("Name", name);
        generator.writeStringField("ContentType", contentType);
        generator.writeStringField("ChangeVector", changeVector);
        generator.writeStringField("Type", "AttachmentPUT");
        generator.writeEndObject();
    }
}
