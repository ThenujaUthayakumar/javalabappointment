package com.javalabappointment.javalabappointment.persist;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Test {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private Integer cost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String search;
}
