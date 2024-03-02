package com.example.mangostore.repository;

import com.example.mangostore.entity.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {
    @Query(value = "select * from origin where status= 1 order by id desc", nativeQuery = true)
    List<Origin> getAllOriginByStatus1();

    @Query(value = "select * from origin where status= 0 order by id desc", nativeQuery = true)
    List<Origin> getAllOriginByStatus0();

    @Query(value = "select * from origin where name_origin like %:searchOrigin%", nativeQuery = true)
    List<Origin> searchOrigin(@Param("searchOrigin") String searchOrigin);
}
