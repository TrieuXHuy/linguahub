package com.huy.linguahub.service.dto.response.error;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDTO {
    private Instant timestamp;
    private int status;
    private String error;
    private Object message;
    private String path;
}
