package com.javalabappointment.javalabappointment.persist;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SystemUser {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String role;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
