package com.huy.linguahub.service.dto.response.category;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResUpdateCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
