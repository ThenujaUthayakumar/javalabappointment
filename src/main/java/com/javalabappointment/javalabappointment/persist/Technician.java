package com.javalabappointment.javalabappointment.persist;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Technician {
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String position;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
