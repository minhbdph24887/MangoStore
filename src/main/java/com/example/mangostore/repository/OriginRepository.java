package com.example.mangostore.repository;

import com.example.mangostore.entity.Origin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {
    @Query(value = "select * from origin where status= 1 order by id desc", nativeQuery = true)
    Page<Origin> getAllOriginByStatus1(Pageable pageable);

    @Query(value = "select * from origin where status= 0 order by id desc", nativeQuery = true)
    Page<Origin> getAllOriginByStatus0(Pageable pageable);

    @Query(value = "select * from origin where name_origin like %:searchOrigin%", nativeQuery = true)
    Page<Origin> searchOrigin(Pageable pageable, @Param("searchOrigin") String searchOrigin);
}
