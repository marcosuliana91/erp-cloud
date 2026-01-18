package com.erp.shared.exception;

/**
 * Exception thrown when attempting to create a duplicate entity.
 */
public class DuplicateEntityException extends DomainException {

    private final String entityName;
    private final String fieldName;
    private final Object fieldValue;

    public DuplicateEntityException(String entityName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: %s", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
