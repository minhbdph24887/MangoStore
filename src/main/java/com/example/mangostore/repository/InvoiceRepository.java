package com.example.mangostore.repository;

import com.example.mangostore.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select * from invoice where id= :idInvoice", nativeQuery = true)
    List<Invoice> getAllInvoiceById(@Param("idInvoice") Long idInvoice);

    @Query(value = "select * from invoice where id_customer= :idCustomer and invoice_form= 'paying' and invoice_status= 0", nativeQuery = true)
    Invoice findInvoiceByIdAccount(@Param("idCustomer") Long idCustomer);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online'", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline(@Param("idAccount") Long idAccount, Pageable pageable);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= 1", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline1(@Param("idAccount") Long idAccount, Pageable pageable);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= 2", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline2(@Param("idAccount") Long idAccount, Pageable pageable);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= 3", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline3(@Param("idAccount") Long idAccount, Pageable pageable);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= 4", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline4(@Param("idAccount") Long idAccount, Pageable pageable);

    @Query(value = "select * from invoice where id_customer= :idAccount and invoice_form= 'online' and invoice_status= 5", nativeQuery = true)
    Page<Invoice> findAllInvoiceOnline5(@Param("idAccount") Long idAccount, Pageable pageable);
}