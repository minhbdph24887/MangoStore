package com.example.mangostore.repository;

import com.example.mangostore.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query(value = "select * from product_detail where status= 1 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus1();

    @Query(value = "select * from product_detail where status= 0 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus0();
}
