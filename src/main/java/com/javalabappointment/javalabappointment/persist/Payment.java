package com.javalabappointment.javalabappointment.persist;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Payment {
    private Integer id;
    private String referenceNumber;
    private AppointmentEntity appointmentId;
    private Integer amount;
    private Long cardNumber;
    private Integer cvv;
    private String expiryDate;
    private Integer status;
    private String cardHolderName;
    private String cardHolderPhoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
