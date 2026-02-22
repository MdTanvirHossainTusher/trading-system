package com.sisimpur.library.utils;

import com.sisimpur.library.payload.common.response.PaginatedResponse;
import com.sisimpur.library.payload.common.response.PaginationMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils() {}

    public static PaginationMetadata buildMetadata(Page<?> page) {
        return PaginationMetadata.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public static <T> PaginatedResponse<T> buildResponse(Page<T> page) {
        return PaginatedResponse.<T>builder()
                .content(page.getContent())
                .pagination(buildMetadata(page))
                .build();
    }

    public static Pageable createPageable(int pageNum, int pageSize, String sortField, boolean descending) {
        Sort sort = descending ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        return PageRequest.of(pageNum, pageSize, sort);
    }
    
    public static Pageable defaultPageable(int page, int limit) {
        return createPageable(page, limit, "id", true);
    }
}
