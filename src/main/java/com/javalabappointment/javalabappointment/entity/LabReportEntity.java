package com.javalabappointment.javalabappointment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="lab_reports")
public class LabReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="reference_number")
    private String referenceNumber;

    @OneToOne(fetch = FetchType.EAGER,optional = true)
    @JoinColumn(name = "appointment_id",referencedColumnName = "id")
    private AppointmentEntity appointmentId;

    @JoinColumn(name = "technician_id")
    private Integer technicianId;

    @Column(name="attachment",columnDefinition = "json")
    private String pdf;

    @Column(name="status")
    private Integer status;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
