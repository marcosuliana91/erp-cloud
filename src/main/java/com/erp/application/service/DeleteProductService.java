package com.erp.application.service;

import com.erp.application.port.in.DeleteProductUseCase;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.Product;
import com.erp.domain.product.ProductId;
import com.erp.shared.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementing the Delete Product use case.
 * Performs logical delete by discontinuing the Product.
 */
@Service
@Transactional
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(UUID id) {
        ProductId productId = ProductId.of(id);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));

        // Logical delete - discontinue the product
        product.discontinue();
        productRepository.save(product);
    }
}
