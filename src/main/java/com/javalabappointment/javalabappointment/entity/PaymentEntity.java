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
@Table(name="payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="reference_number")
    private String referenceNumber;

    @OneToOne(fetch = FetchType.EAGER,optional = true)
    @JoinColumn(name = "appointment_id",referencedColumnName = "id")
    private AppointmentEntity appointmentId;

    @Column(name="amount")
    private Integer amount;

    @Column(name="card_number")
    private Long cardNumber;

    @Column(name="cvv")
    private Integer cvv;

    @Column(name="expiry_date")
    private String expiryDate;

    @Column(name="status")
    private Integer status;

    @Column(name="card_holder_name")
    private String cardHolderName;

    @Column(name="card_holder_phone_number")
    private String cardHolderPhoneNumber;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
