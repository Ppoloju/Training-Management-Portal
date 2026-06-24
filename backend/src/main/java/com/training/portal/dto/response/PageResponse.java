package com.training.portal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Paginated response wrapper")
public record PageResponse<T>(
    
    @Schema(description = "List of items in the current page")
    List<T> content,
    
    @Schema(description = "Current page number (0-indexed)", example = "0")
    int pageNumber,
    
    @Schema(description = "Number of items per page", example = "10")
    int pageSize,
    
    @Schema(description = "Total number of items across all pages", example = "100")
    long totalElements,
    
    @Schema(description = "Total number of pages", example = "10")
    int totalPages,
    
    @Schema(description = "Indicates whether this is the last page")
    boolean last
) {}
