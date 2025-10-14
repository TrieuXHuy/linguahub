package com.huy.linguahub.service.dto.response.category;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResCreateCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Instant createdDate;
}
