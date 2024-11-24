package org.library.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record ListPersonResponse(
        List<PersonResponse> personResponses,
       Integer page,
        Integer pageSize,
        Integer totalPages,
        Long totalCount
) { }