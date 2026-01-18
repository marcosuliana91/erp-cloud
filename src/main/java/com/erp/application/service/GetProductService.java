package com.erp.application.service;

import com.erp.application.port.in.GetProductUseCase;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.Product;
import com.erp.domain.product.ProductId;
import com.erp.shared.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementing the Get Product use case.
 * Retrieves a Product by its identifier.
 */
@Service
@Transactional(readOnly = true)
public class GetProductService implements GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(UUID id) {
        ProductId productId = ProductId.of(id);
        return productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }
}
