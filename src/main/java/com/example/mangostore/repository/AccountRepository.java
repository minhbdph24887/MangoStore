package com.example.mangostore.repository;

import com.example.mangostore.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "select * from accounts where email= :email", nativeQuery = true)
    Account detailAccountByEmail(@Param("email") String email);

    @Query(value = "select * from accounts where status= 1 order by id desc", nativeQuery = true)
    List<Account> getAllAccountByStatus1();

    @Query(value = "select * from accounts where status= 0 order by id desc", nativeQuery = true)
    List<Account> getAllAccountByStatus0();

    @Query(value = "select a.* from accounts a join authentication b on a.id = b.id_account join roles r on b.id_role = r.id where r.id= :idRole", nativeQuery = true)
    List<Account> getAllAccountByRole(@Param("idRole") Long idRole);

    @Query(value = "select case when count(*) > 0 then 1 else 0 end from accounts where email = :email", nativeQuery = true)
    int existsByEmail(@Param("email") String email);
}
