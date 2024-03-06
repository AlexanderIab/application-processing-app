package com.iablonski.processing.search;

import org.springframework.data.domain.Sort;

public record RequestSearchValues(String username,
                                  Integer pageNumber,
                                  String sortDirection){
    public Sort.Direction getSortDirection() {
        if (sortDirection == null ||
                sortDirection.trim().isEmpty() ||
                sortDirection.trim().equalsIgnoreCase("asc")) return Sort.Direction.ASC;
        return Sort.Direction.DESC;
    }
}
