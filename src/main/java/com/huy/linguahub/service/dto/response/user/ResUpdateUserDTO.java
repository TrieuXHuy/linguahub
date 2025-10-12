package com.huy.linguahub.service.dto.response.user;

import com.huy.linguahub.domain.enumeration.Gender;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResUpdateUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Integer age;
    private String address;
    private String imageUrl;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}
