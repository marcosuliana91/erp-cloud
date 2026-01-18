package com.erp.infrastructure.persistence.repository;

import com.erp.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for ProductEntity.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByCode(String code);

    Optional<ProductEntity> findByIntegrationCode(String integrationCode);

    boolean existsByCode(String code);

    boolean existsByIntegrationCode(String integrationCode);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<ProductEntity> findByDescriptionContainingIgnoreCase(@Param("description") String description, Pageable pageable);

    Page<ProductEntity> findByFamily(String family, Pageable pageable);

    Page<ProductEntity> findByBrand(String brand, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.status = 'ACTIVE'")
    Page<ProductEntity> findAllActive(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity <= p.minimumStock AND p.status = 'ACTIVE'")
    Page<ProductEntity> findLowStockProducts(Pageable pageable);
}
