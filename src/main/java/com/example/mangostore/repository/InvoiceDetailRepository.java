package com.example.mangostore.repository;

import com.example.mangostore.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    @Query(value = "select * from invoice_detail where form = 'offline' and id_invoice= :idInvoice", nativeQuery = true)
    List<InvoiceDetail> getAllInvoiceDetailByIdInvoice(@Param("idInvoice") Long idInvoice);

    @Query(value = "select SUM(capital_sum) from invoice_detail di join invoice i on di.id_invoice = i.id where i.id = :idInvoice", nativeQuery = true)
    List<Integer> capitalSumDetailInvoice(@Param("idInvoice") Long idInvoice);

    @Query(value = "select * from invoice_detail where id_product_detail= :idProductDetail and id_invoice= :idInvoice", nativeQuery = true)
    InvoiceDetail findAllByIdInvoiceAndProductDetails(@Param("idProductDetail") Long idProductDetail, @Param("idInvoice") Long idInvoice);

    @Query(value = "select * from invoice_detail where id_invoice= :idInvoice and form= 'online'", nativeQuery = true)
    List<InvoiceDetail> findAllByIdInvoice(@Param("idInvoice") Long id);
}
