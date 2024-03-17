package com.javalabappointment.javalabappointment.repository;

import com.javalabappointment.javalabappointment.entity.SystemUserEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SystemUserRepository extends JpaRepository<SystemUserEntity,Integer> {
    @Query(value = """
            SELECT * FROM system_users
            WHERE (:id IS NULL OR id=:id)
            AND (:name IS NULL OR name=:name)
            AND (:phoneNumber IS NULL OR phone_number=:phoneNumber)
            AND (:address IS NULL OR address=:address)
            AND (:email IS NULL OR email=:email)
            AND (:role IS NULL OR role=:role)
            AND (:username IS NULL OR username=:username)
            AND (:password IS NULL OR password=:password)
            AND (:search IS NULL OR 
                    name LIKE :search OR
                    phone_number LIKE :search OR
                    email LIKE :search OR
                    role LIKE :search OR
                    username LIKE :search OR
                    address LIKE :search
                )
            """,nativeQuery = true)
    Page<SystemUserEntity> findAllUser(Pageable pageable, Integer id, String name, String phoneNumber,
                                 String address, String email,String role,String username,String password,String search);

    @Modifying
    @Query(value = "DELETE FROM system_users WHERE id =?1",nativeQuery = true)
    void deleteById(Integer id);

    @Query(value = "SELECT * FROM system_users WHERE username =?1 AND password=?2",nativeQuery = true)
    SystemUserEntity findByUsername(String username,String password);

}
