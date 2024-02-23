package com.javalabappointment.javalabappointment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="reference_number")
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.EAGER,optional = true)
    @JoinColumn(name = "test_id",referencedColumnName = "id")
    private TestEntity testId;

    @Column(name="name")
    private String name;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="address")
    private String address;

    @Column(name="email")
    private String email;

    @Column(name="age")
    private String age;

    @Column(name="appointment_date_time")
    private String appointmentDateTime;

    @Column(name="doctor_name")
    private String doctorName;

    @Column(name="gender")
    private String gender;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
