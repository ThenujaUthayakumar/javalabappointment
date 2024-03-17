package com.javalabappointment.javalabappointment.persist;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LabReport {
    private Integer id;
    private String referenceNumber;
    private AppointmentEntity appointmentId;
    private Integer technicianId;
    private String pdf;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
