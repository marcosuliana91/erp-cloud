package com.erp.adapters.in.rest.controller;

import com.erp.adapters.in.rest.dto.CreateProductRequest;
import com.erp.adapters.in.rest.dto.ProductResponse;
import com.erp.adapters.in.rest.dto.UpdateProductRequest;
import com.erp.adapters.in.rest.mapper.ProductDtoMapper;
import com.erp.application.port.in.*;
import com.erp.domain.product.Product;
import com.erp.shared.adapter.in.BaseController;
import com.erp.shared.api.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Product operations.
 * Handles HTTP requests and delegates to use cases.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management API")
public class ProductController extends BaseController<CreateProductRequest, ProductResponse, UUID> {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductDtoMapper mapper;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            GetProductUseCase getProductUseCase,
            ListProductsUseCase listProductsUseCase,
            DeleteProductUseCase deleteProductUseCase,
            ProductDtoMapper mapper) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Product with same code already exists")
    })
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        var command = mapper.toCreateCommand(request);
        Product product = createProductUseCase.execute(command);
        ProductResponse response = mapper.toResponse(product);
        return created(response, product.id().value());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates an existing product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Product with same code already exists")
    })
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        var command = mapper.toUpdateCommand(id, request);
        Product product = updateProductUseCase.execute(command);
        return ok(mapper.toResponse(product));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> getById(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        Product product = getProductUseCase.execute(id);
        return ok(mapper.toResponse(product));
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Retrieves a paginated list of products with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<PageResponse<ProductResponse>> list(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "description") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "Filter by description (contains)") @RequestParam(required = false) String description,
            @Parameter(description = "Filter by family") @RequestParam(required = false) String family,
            @Parameter(description = "Filter by brand") @RequestParam(required = false) String brand) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        var query = new ListProductsUseCase.ListProductsQuery(pageable, description, family, brand);

        Page<Product> products = listProductsUseCase.execute(query);
        return pagedResponse(products, mapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Performs a logical delete by discontinuing the product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        deleteProductUseCase.execute(id);
        return noContent();
    }
}
