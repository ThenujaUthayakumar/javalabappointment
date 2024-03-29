package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ContactRepository extends JpaRepository<ContactEntity,Integer> {
    @Query(value = """
        SELECT * FROM contacts c
        WHERE (:id IS NULL OR c.id = :id)
        AND (:name IS NULL OR c.name = :name)
        AND (:address IS NULL OR c.address = :address)
        AND (:phoneNumber IS NULL OR c.phone_number = :phoneNumber)
        AND (:message IS NULL OR c.message = :message)
        AND (:search IS NULL OR 
            c.name LIKE :search OR
            c.address LIKE :search OR
            c.phone_number LIKE :search OR
            c.message LIKE :search
        )
        """, nativeQuery = true)
    Page<ContactEntity> findAllContact(Pageable pageable, Integer id, String name, String address,
                                               String phoneNumber, String message, String search);

    @Modifying
    @Query(value = "DELETE FROM contacts WHERE id =?1",nativeQuery = true)
    void deleteById(Integer id);

    @Query(value = """
        SELECT 
        COUNT(*) AS totalContacts
        FROM contacts c
        WHERE (:id IS NULL OR c.id = :id)
        AND (:name IS NULL OR c.name = :name)
        AND (:address IS NULL OR c.address = :address)
        AND (:phoneNumber IS NULL OR c.phone_number = :phoneNumber)
        AND (:message IS NULL OR c.message = :message)
        AND (:search IS NULL OR 
            c.name LIKE :search OR
            c.address LIKE :search OR
            c.phone_number LIKE :search OR
            c.message LIKE :search
        )
        """, nativeQuery = true)
    Page<Map<Object,String>> findStatistics(Pageable pageable, Integer id, String name, String address,
                                            String phoneNumber, String message, String search);
}
