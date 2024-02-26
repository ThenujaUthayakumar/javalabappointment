package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Integer> {
    @Query(value = """
            SELECT reference_number FROM appointments WHERE reference_number IS NOT NULL ORDER BY id DESC LIMIT 1
            """,nativeQuery = true)
    String findReferenceNumber();

    @Query(value = """
            SELECT * FROM appointments a
            WHERE a.id  = :appointmentId
            """, nativeQuery = true)
    AppointmentEntity findAppointmentWithRecipientEmailById(Integer appointmentId);

    @Query(value = """
        SELECT a.id AS appointment_id, a.name AS appointment_name, t.id AS test_id, t.name AS test_name
        FROM appointments a
        LEFT JOIN tests t ON t.id = a.test_id
        WHERE a.id = :appointmentId
        """, nativeQuery = true)
    AppointmentEntity findTestName(Integer appointmentId);


    @Query(value = """
        SELECT a.* FROM appointments a
        LEFT JOIN tests t ON t.id = a.test_id
        WHERE (:id IS NULL OR a.id = :id)
        AND (:name IS NULL OR a.name = :name)
        AND (:address IS NULL OR a.address = :address)
        AND (:email IS NULL OR a.email = :email)
        AND (:age IS NULL OR a.age = :age)
        AND (:referenceNumber IS NULL OR a.reference_number = :referenceNumber)
        AND (:phoneNumber IS NULL OR a.phone_number = :phoneNumber)
        AND (:appointmentDateTime IS NULL OR a.appointment_date_time = :appointmentDateTime)
        AND (:testId IS NULL OR a.test_id = :testId)
        AND (:doctorName IS NULL OR a.doctor_name = :doctorName)
        AND (:gender IS NULL OR a.gender = :gender)
        AND (:search IS NULL OR 
            a.name LIKE :search OR
            a.address LIKE :search OR
            a.email LIKE :search OR
            a.age LIKE :search OR
            a.reference_number LIKE :search OR
            a.phone_number LIKE :search OR
            t.name LIKE :search OR
            a.doctor_name LIKE :search 
        )
        """, nativeQuery = true)
    Page<AppointmentEntity> findAllAppointment(Pageable pageable, Integer id, String name, String address,
                                               String email, String age, String referenceNumber, String phoneNumber,
                                               String appointmentDateTime, TestEntity testId, String doctorName,
                                               String search,String gender);

}
