package com.huy.linguahub.domain;

import com.huy.linguahub.config.Constants;
import com.huy.linguahub.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractAuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email must not be blank!")
    @Pattern(regexp = Constants.LOGIN_REGEX, message = "Invalid email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank!")
    private String password;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    private String address;

    private String imageUrl;

    @Override
    public Long getId() {
        return id;
    }
}
