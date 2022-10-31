package com.anonimos.springboot.app.lessors.models.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "lessors_cars")
public class LessorCar implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="car_id", unique = true)
    private Long carId;

    @Override
    public boolean equals(Object obj) {
        if(this  == obj)
            return true;

        if(!(obj instanceof LessorCar))
            return false;

        LessorCar o = (LessorCar) obj;
        return this.carId != null && this.carId.equals(o.carId);
    }
}
