package com.example.mangostore.repository;

import com.example.mangostore.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    @Query(value = "select * from color where status= 1 order by id desc", nativeQuery = true)
    List<Color> getAllColorByStatus1();

    @Query(value = "select * from color where status= 0 order by id desc", nativeQuery = true)
    List<Color> getAllColorByStatus0();

    @Query(value = "select * from color where name_color like %:searchColor%", nativeQuery = true)
    List<Color> searchColor(@Param("searchColor") String searchColor);
}
