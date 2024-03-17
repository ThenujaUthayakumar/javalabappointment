package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.ContactEntity;
import com.javalabappointment.javalabappointment.entity.LabReportEntity;
import com.javalabappointment.javalabappointment.persist.LabReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;

public interface LabReportRepository extends JpaRepository<LabReportEntity,Integer> {
    @Query(value = """
            SELECT reference_number FROM lab_reports WHERE reference_number IS NOT NULL ORDER BY id DESC LIMIT 1
            """,nativeQuery = true)
    String findReferenceNumber();

    @Modifying
    @Query(value = "DELETE FROM lab_reports WHERE id =?1",nativeQuery = true)
    void deleteById(Integer id);

    @Query(value = """
        SELECT  
        l.reference_number AS labNo,
        l.created_at AS date,
        a.name AS patientName,
        a.phone_number AS patientMobile,
        a.reference_number AS patientNo,
        te.name AS test,
        t.name AS technician,
        l.attachment AS file,
        l.status AS status,
        a.age AS age,
        a.appointment_date_time AS appointmentDate,
        a.email AS email,
        l.id AS id
        FROM lab_reports l
        LEFT JOIN appointments a ON a.id=l.appointment_id
        LEFT JOIN tests te ON te.id=a.test_id
        LEFT JOIN technicians t ON t.id=l.technician_id
        WHERE (:id IS NULL OR l.id = :id)
        AND (:referenceNumber IS NULL OR l.reference_number = :referenceNumber)
        AND (:technicianId IS NULL OR l.technician_id = :technicianId)
        AND (:appointmentId IS NULL OR l.appointment_id = :appointmentId)
        AND (:pdf IS NULL OR l.attachment = :pdf)
        AND (:status IS NULL OR l.status = :status)
        AND (:search IS NULL OR 
            l.reference_number LIKE :search OR
            a.reference_number LIKE :search OR
            te.name LIKE :search OR
            l.status LIKE :search OR
            a.name LIKE :search OR
            te.name LIKE :search OR
            a.phone_number LIKE :search OR
            t.name LIKE :search
        )
        """, nativeQuery = true)
    Page<Map<Object,String>> findAllReport(Pageable pageable, Integer id, String referenceNumber,Integer technicianId,
                                           AppointmentEntity appointmentId,String pdf,Integer status,
                                            String search);

    @Query(value = """
        SELECT  
        COUNT(*) AS totalReports
        FROM lab_reports l
        LEFT JOIN appointments a ON a.id=l.appointment_id
        LEFT JOIN tests te ON te.id=a.test_id
        LEFT JOIN technicians t ON t.id=l.technician_id
        WHERE (:id IS NULL OR l.id = :id)
        AND (:referenceNumber IS NULL OR l.reference_number = :referenceNumber)
        AND (:technicianId IS NULL OR l.technician_id = :technicianId)
        AND (:appointmentId IS NULL OR l.appointment_id = :appointmentId)
        AND (:pdf IS NULL OR l.attachment = :pdf)
        AND (:status IS NULL OR l.status = :status)
        AND (:search IS NULL OR 
            l.reference_number LIKE :search OR
            a.reference_number LIKE :search OR
            te.name LIKE :search OR
            l.status LIKE :search OR
            a.name LIKE :search OR
            te.name LIKE :search OR
            a.phone_number LIKE :search OR
            t.name LIKE :search
        )
        """, nativeQuery = true)
    Page<Map<Object,String>> findStatistics(Pageable pageable, Integer id, String referenceNumber,Integer technicianId,
                                           AppointmentEntity appointmentId,String pdf,Integer status,
                                           String search);

    Optional<LabReport> findById(Long labReportId);
}
