package com.huy.linguahub.service.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResUserLoginDTO {
    private Long id;
    private String email;
    private String fullName;
}
