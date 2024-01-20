package com.example.mangostore.repository;

import com.example.mangostore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where status= 1 order by id desc", nativeQuery = true)
    Page<Product> getAllProductByStatus1(Pageable pageable);

    @Query(value = "select * from product where status= 0 order by id desc", nativeQuery = true)
    Page<Product> getAllProductByStatus0(Pageable pageable);

    @Query(value = "select * from product where name_product like %:searchProduct%", nativeQuery = true)
    Page<Product> searchProduct(Pageable pageable, @Param("searchProduct") String searchProduct);
}
