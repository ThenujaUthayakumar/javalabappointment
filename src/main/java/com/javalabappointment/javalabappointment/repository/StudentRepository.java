package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity,Integer> {
}
