package com.example.mangostore.repository;

import com.example.mangostore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(value = "select * from shopping_cart where id_account= :idAccount", nativeQuery = true)
    ShoppingCart findByIdAccount(@Param("idAccount") Long id);
}
