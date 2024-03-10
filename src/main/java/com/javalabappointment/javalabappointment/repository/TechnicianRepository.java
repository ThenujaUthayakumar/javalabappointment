package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TechnicianRepository extends JpaRepository<TechnicianEntity,Integer> {

    @Query(value = """
        SELECT * FROM technicians t
        WHERE (:id IS NULL OR t.id = :id)
        AND (:name IS NULL OR t.name = :name)
        AND (:address IS NULL OR t.address = :address)
        AND (:phoneNumber IS NULL OR t.phone_number = :phoneNumber)
        AND (:search IS NULL OR 
            t.name LIKE :search OR
            t.address LIKE :search OR
            t.phone_number LIKE :search
        )
        """, nativeQuery = true)
    Page<TechnicianEntity> findAllTechnician(Pageable pageable, Integer id, String name, String address,
                                       String phoneNumber,String search);
}
