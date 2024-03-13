package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.TestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TestRepository extends JpaRepository<TestEntity,Integer> {
    @Query(value = """
            SELECT * FROM tests
            WHERE (:id IS NULL OR id=:id)
            AND (:name IS NULL OR name=:name)
            AND (:description IS NULL OR description=:description)
            AND (:cost IS NULL OR cost=:cost)
            AND (:search IS NULL OR 
                    name LIKE :search OR
                    description LIKE :search OR
                    code LIKE :search
                )
            """,nativeQuery = true)
    Page<TestEntity> findAllTest(Pageable pageable,Integer id,String name,String description,
                                 Integer cost,String search);

    @Query(value = """
            SELECT code FROM tests WHERE code IS NOT NULL ORDER BY id DESC LIMIT 1
            """,nativeQuery = true)
    String findTestCode();

    @Modifying
    @Query(value = "DELETE FROM tests WHERE id =?1",nativeQuery = true)
    void deleteById(Integer id);

    @Query(value = """
            SELECT
            COUNT(*) AS totalTests
            FROM tests
            WHERE (:id IS NULL OR id=:id)
            AND (:name IS NULL OR name=:name)
            AND (:description IS NULL OR description=:description)
            AND (:cost IS NULL OR cost=:cost)
            AND (:search IS NULL OR 
                    name LIKE :search OR
                    description LIKE :search OR
                    code LIKE :search
                )
            """,nativeQuery = true)
    Page<Map<Object,String>> findStatistics(Pageable pageable, Integer id, String name, String description,
                                            Integer cost, String search);
}
