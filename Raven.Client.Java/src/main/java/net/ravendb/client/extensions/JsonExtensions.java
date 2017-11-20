package net.ravendb.client.extensions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.queries.IndexQuery;
import net.ravendb.client.documents.session.EntityToJson;
import net.ravendb.client.primitives.NetDateFormat;
import net.ravendb.client.primitives.SharpAwareJacksonAnnotationIntrospector;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class JsonExtensions {

    private static ObjectMapper _defaultMapper;

    public static ObjectMapper getDefaultMapper() {
        if (_defaultMapper == null) {
            synchronized (JsonExtensions.class) {
                if (_defaultMapper == null) {
                    _defaultMapper = createDefaultJsonSerializer();
                }
            }
        }

        return _defaultMapper;
    }

    public static ObjectMapper createDefaultJsonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new DotNetNamingStrategy());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.setConfig(objectMapper.getSerializationConfig().with(new NetDateFormat()));
        objectMapper.setConfig(objectMapper.getDeserializationConfig().with(new NetDateFormat()));
        objectMapper.setAnnotationIntrospector(new SharpAwareJacksonAnnotationIntrospector());
        return objectMapper;
    }

    public static class DotNetNamingStrategy extends PropertyNamingStrategy {

        @Override
        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return StringUtils.capitalize(defaultName);
        }

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return StringUtils.capitalize(defaultName);
        }

        @Override
        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return StringUtils.capitalize(defaultName);
        }

        @Override
        public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
            return StringUtils.capitalize(defaultName);
        }
    }

    public static void writeIndexQuery(JsonGenerator generator, DocumentConventions conventions, IndexQuery query) throws IOException {
        generator.writeStartObject();

        generator.writeStringField("Query", query.getQuery());

        if (query.isPageSizeSet() && query.getPageSize() >= 0) {
            generator.writeNumberField("PageSize", query.getPageSize());
        }

        if (query.isWaitForNonStaleResults()) {
            generator.writeBooleanField("WaitForNonStaleResults", query.isWaitForNonStaleResults());
        }

        if (query.getCutoffEtag() != null) {
            generator.writeNumberField("CutoffEtag", query.getCutoffEtag());
        }

        if (query.getStart() > 0) {
            generator.writeNumberField("Start", query.getStart());
        }

        /* TODO


            if (query.WaitForNonStaleResultsTimeout.HasValue)
            {
                writer.WritePropertyName(nameof(query.WaitForNonStaleResultsTimeout));
                writer.WriteString(query.WaitForNonStaleResultsTimeout.Value.ToInvariantString());
                writer.WriteComma();
            }

            if (query.DisableCaching)
            {
                writer.WritePropertyName(nameof(query.DisableCaching));
                writer.WriteBool(query.DisableCaching);
                writer.WriteComma();
            }

            if (query.ExplainScores)
            {
                writer.WritePropertyName(nameof(query.DisableCaching));
                writer.WriteBool(query.DisableCaching);
                writer.WriteComma();
            }

            if (query.ShowTimings)
            {
                writer.WritePropertyName(nameof(query.ShowTimings));
                writer.WriteBool(query.ShowTimings);
                writer.WriteComma();
            }

            if (query.SkipDuplicateChecking)
            {
                writer.WritePropertyName(nameof(query.SkipDuplicateChecking));
                writer.WriteBool(query.SkipDuplicateChecking);
                writer.WriteComma();
            }
*/
        generator.writeFieldName("QueryParameters");
        if (query.getQueryParameters() != null) {
            generator.writeObject(EntityToJson.convertEntityToJson(query.getQueryParameters(), conventions));
        } else {
            generator.writeNull();
        }

        generator.writeEndObject();
    }

}
