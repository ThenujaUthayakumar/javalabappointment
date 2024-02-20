package com.javalabappointment.javalabappointment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javalabappointment.javalabappointment.service.StudentService;
import com.javalabappointment.javalabappointment.entity.StudentEntity;
import com.javalabappointment.javalabappointment.persist.Student;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/public") // Mapping for POST requests to /student/public
    public ResponseEntity<StudentEntity> store(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.store(student));
    }


}
