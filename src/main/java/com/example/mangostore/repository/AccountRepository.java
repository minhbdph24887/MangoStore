package com.example.mangostore.repository;

import com.example.mangostore.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "select * from accounts where email= :email ", nativeQuery = true)
    Account detailAccountByEmail(@Param("email") String email);

    @Query(value = "select * from accounts where status= 1", nativeQuery = true)
    Page<Account> getAllAccountByStatus1(Pageable pageable);

    @Query(value = "select * from accounts where status= 0", nativeQuery = true)
    Page<Account> getAllAccountByStatus0(Pageable pageable);
}
