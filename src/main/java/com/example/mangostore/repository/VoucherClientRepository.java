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
    @Query(value = "select * from voucher_client where id_account= :idAccount and id_voucher= :idVoucher and voucher_status in (1, 2)", nativeQuery = true)
    VoucherClient voucherClientByAccountAndVoucher(@Param("idAccount") Long idAccount, @Param("idVoucher") Long idVoucher);

    @Query(value = "select vc.* from voucher_client vc join accounts a on vc.id_account = a.id where a.id_rank = :rankAccount and vc.voucher_status= 1 and vc.status= 1", nativeQuery = true)
    List<VoucherClient> findAllVoucherStatusVoucher1(@Param("rankAccount") Long rankAccount);

    @Query(value = "select vc.* from voucher_client vc join accounts a on vc.id_account = a.id where a.id_rank = :rankAccount and vc.voucher_status in (1, 2) and vc.status= 1 and vc.id_account= :idAccount", nativeQuery = true)
    List<VoucherClient> findAllVoucherForClient(@Param("rankAccount") Long rankAccount, @Param("idAccount") Long idAccount);
}
