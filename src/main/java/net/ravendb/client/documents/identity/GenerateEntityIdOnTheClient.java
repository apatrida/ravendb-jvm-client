package net.ravendb.client.documents.identity;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.exceptions.RavenException;
import net.ravendb.client.primitives.Reference;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

public class GenerateEntityIdOnTheClient {

    private final DocumentConventions _conventions;
    private final GenerateIdFunction _generateId;

    public GenerateEntityIdOnTheClient(DocumentConventions conventions, GenerateIdFunction generateId) {
        this._conventions = conventions;
        this._generateId = generateId;
    }

    private Field getIdentityProperty(Class<?> entityType) {
        return _conventions.getIdentityProperty(entityType);
    }

    /**
     * Attempts to get the document key from an instance
     * @param entity Entity to get id from
     * @param idHolder output parameter which holds document id
     * @return true if id was read from entity
     */
    public boolean tryGetIdFromInstance(String collectionName, Object entity, Reference<String> idHolder) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        if (entity instanceof ObjectNode) {
            ObjectNode objEntity = (ObjectNode)entity;
            String idPropName = _conventions.getFindIdentityPropertyNameFromCollectionName().apply(collectionName);
            if (idPropName != null && objEntity.has(idPropName) && objEntity.path(idPropName).isTextual()) {
               idHolder.value = objEntity.get(idPropName).textValue();
            }
            return false;
        }

        try {
            Field identityProperty = getIdentityProperty(entity.getClass());
            if (identityProperty != null) {
                Object value = FieldUtils.readField(identityProperty, entity, true);
                if (value instanceof String) {
                    idHolder.value = (String)value;
                    return true;
                }
            }
            idHolder.value = null;
            return false;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Tries to get the identity.
     * @param entity Entity
     * @return Document id
     */
    public String getOrGenerateDocumentId(String collectionName, Object entity) {
        Reference<String> idHolder = new Reference<>();
        tryGetIdFromInstance(collectionName, entity, idHolder);
        String id = idHolder.value;
        if (id == null) {
            // Generate the key up front
            id = _generateId.apply(collectionName, entity);
        }

        if (id != null && id.startsWith("/")) {
            throw new IllegalStateException("Cannot use value '" + id + "' as a document id because it begins with a '/'");
        }
        return id;
    }

    public String generateDocumentKeyForStorage(String collectionName, Object entity) {
        String id = getOrGenerateDocumentId(collectionName, entity);
        trySetIdentity(collectionName, entity, id);
        return id;
    }

    public void trySetIdentity(String collectionName, Object entity, String id) {
        trySetIdentity(collectionName, entity, id, false);
    }

    /**
     * Tries to set the identity property
     * @param entity Entity
     * @param id Id to set
     * @param isProjection Is projection
     */
    public void trySetIdentity(String collectionName, Object entity, String id, boolean isProjection) {
        trySetIdentityInternal(collectionName, entity, id, isProjection);
    }

    private void trySetIdentityInternal(String collectionName, Object entity, String id, boolean isProjection) {
        if (entity instanceof ObjectNode) {
            // TODO: we don't do this since it is in the metadata
//            ObjectNode objEntity = (ObjectNode)entity;
//            String idPropName = _conventions.getFindIdentityPropertyNameFromCollectionName().apply(collectionName);
//
//            if (isProjection && objEntity.has(idPropName) && objEntity.path(idPropName).isTextual()) {
//                // identity property was already set
//                return;
//            }
//            objEntity.put(idPropName, id);
            return;
        }

        Class<?> entityType = entity.getClass();
        Field identityProperty = _conventions.getIdentityProperty(entityType);

        if (identityProperty == null) {
            return;
        }

        try {
            if (isProjection && FieldUtils.readField(identityProperty, entity) != null) {
                // identity property was already set
                return;
            }
        } catch (IllegalAccessException e) {
            throw new RavenException("Unable to read identity field: " + e.getMessage(), e);
        }

        setPropertyOrField(identityProperty.getType(), entity, identityProperty, id);
    }

    private void setPropertyOrField(Class<?> propertyOrFieldType, Object entity, Field field, String id) {
        try {
            if (String.class.equals(propertyOrFieldType)) {
                FieldUtils.writeField(field, entity, id, true);
            } else {
                throw new IllegalArgumentException("Cannot set identity value '" + id + "' on field " + propertyOrFieldType +
                        " because field type is not string.");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
