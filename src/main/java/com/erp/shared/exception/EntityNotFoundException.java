package com.erp.shared.exception;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends DomainException {

    private final String entityName;
    private final Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s not found with id: %s", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }
}
