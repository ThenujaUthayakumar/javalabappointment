package com.javalabappointment.javalabappointment.persist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String address;
    private String message;
    private String search;
}
