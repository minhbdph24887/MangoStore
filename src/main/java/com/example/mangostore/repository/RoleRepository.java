package com.example.mangostore.repository;

import com.example.mangostore.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from roles where name= 'USER'", nativeQuery = true)
    Role getAllRoleByUser();

    @Query(value = "select r.* from accounts a join authentication b on a.id = b.id_account join roles r on b.id_role = r.id where a.email= :email", nativeQuery = true)
    Role getRoleByEmail(@Param("email") String email);

    @Query(value = "select * from roles where status= 1 order by id desc", nativeQuery = true)
    Page<Role> getAllRoleByStatus1(Pageable pageable);

    @Query(value = "select * from roles where status= 0 order by id desc", nativeQuery = true)
    Page<Role> getAllRoleByStatus0(Pageable pageable);

    @Query(value = "select * from roles where status= 1", nativeQuery = true)
    List<Role> getAllRole();
}
