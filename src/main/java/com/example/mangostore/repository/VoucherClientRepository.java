package com.example.mangostore.repository;

import com.example.mangostore.entity.VoucherClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherClientRepository extends JpaRepository<VoucherClient, Long> {
    @Query(value = "select * from voucher_client where id_account= :idAccount and id_voucher= :idVoucher and voucher_status in (1, 2) and status= 1", nativeQuery = true)
    VoucherClient voucherClientByAccountAndVoucher(@Param("idAccount") Long idAccount, @Param("idVoucher") Long idVoucher);
}
