package com.example.mangostore.repository;

import com.example.mangostore.entity.VoucherClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherClientRepository extends JpaRepository<VoucherClient, Long> {
}
