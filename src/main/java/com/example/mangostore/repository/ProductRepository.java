package com.example.mangostore.repository;

import com.example.mangostore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where status= 1 order by id desc", nativeQuery = true)
    List<Product> getAllProductByStatus1();

    @Query(value = "select * from product where status= 0 order by id desc", nativeQuery = true)
    List<Product> getAllProductByStatus0();

    @Query(value = "select * from product where name_product like %:searchProduct%", nativeQuery = true)
    List<Product> searchProduct(@Param("searchProduct") String searchProduct);
}
