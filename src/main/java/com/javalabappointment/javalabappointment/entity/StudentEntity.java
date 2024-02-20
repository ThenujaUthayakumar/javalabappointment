package com.javalabappointment.javalabappointment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="students")
public class StudentEntity {
    @Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;

    @Column(name="name")
    private String name;
}
