package com.erp.shared.adapter.in;

import com.erp.shared.api.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * Base controller with common REST operations.
 *
 * @param <REQ> request DTO type
 * @param <RES> response DTO type
 * @param <ID> entity identifier type
 */
public abstract class BaseController<REQ, RES, ID> {

    protected ResponseEntity<RES> created(RES response, ID id) {
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    protected ResponseEntity<RES> ok(RES response) {
        return ResponseEntity.ok(response);
    }

    protected ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected <T> ResponseEntity<PageResponse<RES>> pagedResponse(
            Page<T> page,
            Function<T, RES> mapper) {
        List<RES> content = page.getContent().stream()
            .map(mapper)
            .toList();
        return ResponseEntity.ok(PageResponse.of(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        ));
    }

    protected Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    protected Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }
}
