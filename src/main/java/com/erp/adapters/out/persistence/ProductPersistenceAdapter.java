package com.erp.adapters.out.persistence;

import com.erp.adapters.out.persistence.mapper.ProductPersistenceMapper;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.Product;
import com.erp.domain.product.ProductCode;
import com.erp.domain.product.ProductId;
import com.erp.infrastructure.persistence.entity.ProductEntity;
import com.erp.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing ProductRepository port using JPA.
 * Converts between domain and persistence models.
 */
@Repository
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    public ProductPersistenceAdapter(ProductJpaRepository jpaRepository, ProductPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.value())
            .map(mapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        Page<ProductEntity> entityPage = jpaRepository.findAll(pageable);
        return mapToProductPage(entityPage);
    }

    @Override
    public boolean existsById(ProductId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ProductId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public Optional<Product> findByCode(ProductCode code) {
        return jpaRepository.findByCode(code.value())
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findByIntegrationCode(String integrationCode) {
        return jpaRepository.findByIntegrationCode(integrationCode)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsByCode(ProductCode code) {
        return jpaRepository.existsByCode(code.value());
    }

    @Override
    public boolean existsByIntegrationCode(String integrationCode) {
        return jpaRepository.existsByIntegrationCode(integrationCode);
    }

    @Override
    public Page<Product> findByDescriptionContaining(String description, Pageable pageable) {
        Page<ProductEntity> entityPage = jpaRepository.findByDescriptionContainingIgnoreCase(description, pageable);
        return mapToProductPage(entityPage);
    }

    @Override
    public Page<Product> findByFamily(String family, Pageable pageable) {
        Page<ProductEntity> entityPage = jpaRepository.findByFamily(family, pageable);
        return mapToProductPage(entityPage);
    }

    @Override
    public Page<Product> findByBrand(String brand, Pageable pageable) {
        Page<ProductEntity> entityPage = jpaRepository.findByBrand(brand, pageable);
        return mapToProductPage(entityPage);
    }

    private Page<Product> mapToProductPage(Page<ProductEntity> entityPage) {
        List<Product> products = entityPage.getContent().stream()
            .map(mapper::toDomain)
            .toList();
        return new PageImpl<>(products, entityPage.getPageable(), entityPage.getTotalElements());
    }
}
