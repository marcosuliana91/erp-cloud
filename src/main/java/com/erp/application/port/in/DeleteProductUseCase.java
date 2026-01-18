package com.erp.application.port.in;

import java.util.UUID;

/**
 * Use case port for deleting a Product (logical delete).
 */
public interface DeleteProductUseCase {

    void execute(UUID id);
}
