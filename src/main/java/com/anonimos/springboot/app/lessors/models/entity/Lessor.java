package com.anonimos.springboot.app.lessors.models.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "lessors")
@Getter @Setter
@With
@NoArgsConstructor @AllArgsConstructor
public class Lessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "email",unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "user_name")
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

}
