package com.javalabappointment.javalabappointment.service;

import com.javalabappointment.javalabappointment.entity.StudentEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javalabappointment.javalabappointment.persist.Student;
import com.javalabappointment.javalabappointment.repository.StudentRepository;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Transactional
    public StudentEntity store(Student student)
    {
        StudentEntity studentEntity=new StudentEntity();

        if(student.getName() == null || student.getName().isEmpty())
        {
            throw new IllegalStateException("name");
        }

        studentEntity.setName(student.getName());

        return studentRepository.save(studentEntity);
    }
}
