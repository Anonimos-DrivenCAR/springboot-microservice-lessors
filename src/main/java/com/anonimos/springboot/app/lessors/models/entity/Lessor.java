package com.anonimos.springboot.app.lessors.models.entity;

import com.anonimos.springboot.app.lessors.models.Car;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessors")
@Getter @Setter
@With
@NoArgsConstructor @AllArgsConstructor
public class Lessor implements Serializable {
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

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "lessor_id")
    private List<LessorCar> lessorCars = new ArrayList<>();

    @Transient
    private List<Car> cars = new ArrayList<>();

    public void addLessorCar(LessorCar lessorCar){
        lessorCars.add(lessorCar);
    }
    public void removeLessorCar(LessorCar lessorCar){
        lessorCars.remove(lessorCar);
    }

}
