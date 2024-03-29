package com.example.mangostore.repository;

import com.example.mangostore.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = "select * from voucher where voucher_status= 1 and status= 1 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus1();

    @Query(value = "select * from voucher where voucher_status in (0, 1, 2) and status= 0 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus0();

    @Query(value = "select * from voucher where voucher_status in (1, 2) and status = 1 order by id desc", nativeQuery = true)
    List<Voucher> getAllVoucherByStatus1And2();

    @Query(value = "select * from voucher where voucher_status in (1, 2) and name_voucher like %:searchVoucher% and status= 1", nativeQuery = true)
    List<Voucher> searchVoucher(@Param("searchVoucher") String searchVoucher);

    @Query(value = "select * from voucher where voucher_status = 1 and status = 1 and voucher_from = 'offline' and start_day <= cast(getdate() as date) and end_date >= cast(getdate() as date) and id_rank = :idRank", nativeQuery = true)
    List<Voucher> findVoucherByVoucherFrom(@Param("idRank") Long idRank);

    @Query(value = "select * from voucher where voucher_status in (1, 2) and status= 1 and voucher_from= 'online'", nativeQuery = true)
    List<Voucher> getAllVoucherOnline();
}
