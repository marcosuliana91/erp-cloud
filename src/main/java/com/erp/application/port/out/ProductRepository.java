package com.erp.application.port.out;

import com.erp.domain.product.Product;
import com.erp.domain.product.ProductCode;
import com.erp.domain.product.ProductId;
import com.erp.shared.application.port.out.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Product repository port (output port).
 * Defines the contract for Product persistence operations.
 */
public interface ProductRepository extends Repository<Product, ProductId> {

    Optional<Product> findByCode(ProductCode code);

    Optional<Product> findByIntegrationCode(String integrationCode);

    boolean existsByCode(ProductCode code);

    boolean existsByIntegrationCode(String integrationCode);

    Page<Product> findByDescriptionContaining(String description, Pageable pageable);

    Page<Product> findByFamily(String family, Pageable pageable);

    Page<Product> findByBrand(String brand, Pageable pageable);
}
