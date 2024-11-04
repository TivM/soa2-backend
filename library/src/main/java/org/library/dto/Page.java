package org.library.dto;

import java.util.List;

public record Page<T> (List<T> objects, Integer page, Integer pageSize, Integer totalPages, Long totalCount) {
}
