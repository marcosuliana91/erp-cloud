package com.erp.application.port.in;

import com.erp.domain.product.Product;

import java.util.UUID;

/**
 * Use case port for getting a Product by ID.
 */
public interface GetProductUseCase {

    Product execute(UUID id);
}
