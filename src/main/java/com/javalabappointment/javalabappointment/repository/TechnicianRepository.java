package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

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
            t.phone_number LIKE :search OR
            t.email LIKE :search OR
            t.position LIKE :search OR
            t.join_date LIKE :search
        )
        """, nativeQuery = true)
    Page<TechnicianEntity> findAllTechnician(Pageable pageable, Integer id, String name, String address,
                                       String phoneNumber,String search);

    @Modifying
    @Query(value = "DELETE FROM technicians WHERE id =?1",nativeQuery = true)
    void deleteById(Integer id);

    @Query(value = """
        SELECT
        COUNT(*) AS totalTechnicians
        FROM technicians t
        WHERE (:id IS NULL OR t.id = :id)
        AND (:name IS NULL OR t.name = :name)
        AND (:address IS NULL OR t.address = :address)
        AND (:phoneNumber IS NULL OR t.phone_number = :phoneNumber)
        AND (:search IS NULL OR 
            t.name LIKE :search OR
            t.address LIKE :search OR
            t.phone_number LIKE :search OR
            t.email LIKE :search OR
            t.position LIKE :search OR
            t.join_date LIKE :search
        )
        """, nativeQuery = true)
    Page<Map<Object,String>> findStatistics(Pageable pageable, Integer id, String name, String address,
                                            String phoneNumber, String search);
}
