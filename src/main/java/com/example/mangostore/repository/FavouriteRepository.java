package com.example.mangostore.repository;

import com.example.mangostore.entity.Favourite;
import com.example.mangostore.entity.FavouriteDetail;
import com.example.mangostore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    @Query(value = "select * from favourite where id_account= :idAccount", nativeQuery = true)
    Favourite findByIdAccount(@Param("idAccount") Long id);
}
