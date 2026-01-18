package com.erp.shared.domain;

/**
 * Base marker interface for domain entities.
 * Domain entities have identity and lifecycle.
 *
 * @param <ID> the type of the entity identifier
 */
public interface DomainEntity<ID> {

    /**
     * Returns the unique identifier of this entity.
     */
    ID id();
}
