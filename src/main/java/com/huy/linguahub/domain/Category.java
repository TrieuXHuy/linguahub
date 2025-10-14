package com.huy.linguahub.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends AbstractAuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name must not be blank!")
    private String name;

    @NotBlank(message = "Description must not be blank!")
    private String description;

    // courseId

    @Override
    public Long getId() {
        return id;
    }
}
