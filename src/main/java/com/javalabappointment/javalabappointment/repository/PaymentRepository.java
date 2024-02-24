package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    @Query(value = """
            SELECT reference_number FROM payments WHERE reference_number IS NOT NULL ORDER BY id DESC LIMIT 1
            """,nativeQuery = true)
    String findReferenceNumber();

    PaymentEntity findByAppointmentId(AppointmentEntity appointmentId);

    @Query(value = """
        SELECT p.* FROM payments p
        LEFT JOIN appointments a ON a.id=p.appointment_id
        LEFT JOIN tests t ON t.id = a.test_id
        WHERE (:id IS NULL OR p.id = :id)
        AND (:amount IS NULL OR p.amount = :amount)
        AND (:appointmentId IS NULL OR p.appointment_id = :appointmentId)
        AND (:status IS NULL OR p.status = :status)
        AND (:referenceNumber IS NULL OR p.reference_number = :referenceNumber)
        AND (:search IS NULL OR 
            a.name LIKE :search OR
            a.address LIKE :search OR
            a.email LIKE :search OR
            a.age LIKE :search OR
            a.reference_number LIKE :search OR
            a.phone_number LIKE :search OR
            t.name LIKE :search OR
            a.doctor_name LIKE :search OR
            p.reference_number LIKE :search OR
            p.card_holder_name LIKE :search
        )
        """, nativeQuery = true)
    Page<PaymentEntity> findAllPayment(Pageable pageable, Integer id, Integer amount, AppointmentEntity appointmentId,
                                               String referenceNumber, Integer status,String search);
}
