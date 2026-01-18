package com.erp.application.port.in;

import com.erp.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case port for listing Products with pagination.
 */
public interface ListProductsUseCase {

    Page<Product> execute(ListProductsQuery query);

    record ListProductsQuery(
        Pageable pageable,
        String description,
        String family,
        String brand
    ) {
        public static ListProductsQuery of(Pageable pageable) {
            return new ListProductsQuery(pageable, null, null, null);
        }

        public boolean hasDescriptionFilter() {
            return description != null && !description.isBlank();
        }

        public boolean hasFamilyFilter() {
            return family != null && !family.isBlank();
        }

        public boolean hasBrandFilter() {
            return brand != null && !brand.isBlank();
        }
    }
}
