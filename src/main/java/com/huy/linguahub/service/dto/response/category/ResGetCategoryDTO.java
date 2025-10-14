package com.huy.linguahub.service.dto.response.category;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResGetCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
