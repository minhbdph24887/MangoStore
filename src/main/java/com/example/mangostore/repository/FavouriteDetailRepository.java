package com.example.mangostore.repository;

import com.example.mangostore.entity.FavouriteDetail;
import com.example.mangostore.entity.ShoppingCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteDetailRepository extends JpaRepository<FavouriteDetail, Long> {
    @Query(value = "select * from favourite_detail where id_favourite= :favourite", nativeQuery = true)
    List<FavouriteDetail> getAllFavourite(@Param("favourite") Long id);
}
