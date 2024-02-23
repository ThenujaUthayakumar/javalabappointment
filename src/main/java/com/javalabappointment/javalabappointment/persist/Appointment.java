package com.javalabappointment.javalabappointment.persist;

import com.javalabappointment.javalabappointment.entity.TestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Appointment {
    private Integer id;
    private String referenceNumber;
    private TestEntity testId;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String age;
    private String appointmentDateTime;
    private String doctorName;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
