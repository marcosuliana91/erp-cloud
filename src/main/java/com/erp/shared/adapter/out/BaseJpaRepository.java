package com.erp.shared.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Base JPA repository interface.
 * Extends Spring Data JPA with UUID as the default ID type.
 *
 * @param <T> the entity type
 */
@NoRepositoryBean
public interface BaseJpaRepository<T> extends JpaRepository<T, UUID> {
}
