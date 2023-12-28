package com.example.mangostore.repository;

import com.example.mangostore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from roles where name= 'ROLE_USER'", nativeQuery = true)
    Role getAllRoleByUser();
}
