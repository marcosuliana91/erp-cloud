package com.erp.application.service;

import com.erp.application.port.in.ListProductsUseCase;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementing the List Products use case.
 * Retrieves paginated Products with optional filters.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListProductsService implements ListProductsUseCase {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> execute(ListProductsQuery query) {
        if (query.hasDescriptionFilter()) {
            return productRepository.findByDescriptionContaining(query.description(), query.pageable());
        }

        if (query.hasFamilyFilter()) {
            return productRepository.findByFamily(query.family(), query.pageable());
        }

        if (query.hasBrandFilter()) {
            return productRepository.findByBrand(query.brand(), query.pageable());
        }

        return productRepository.findAll(query.pageable());
    }
}
