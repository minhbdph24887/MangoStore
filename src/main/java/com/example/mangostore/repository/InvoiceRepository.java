package com.example.mangostore.repository;

import com.example.mangostore.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select * from invoice where invoice_status= 0 and id_account= :idAccount", nativeQuery = true)
    List<Invoice> getAllInvoiceByAccount(@Param("idAccount") Long idAccount);

    @Query(value = "select top 1 name_invoice from invoice order by name_invoice desc", nativeQuery = true)
    String getMaxInvoiceCode();
}
