package com.erp.shared.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Generic repository port (output port).
 * Defines the contract for persistence operations.
 *
 * @param <T> the domain entity type
 * @param <ID> the identifier type
 */
public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    Page<T> findAll(Pageable pageable);

    boolean existsById(ID id);

    void deleteById(ID id);

    long count();
}
