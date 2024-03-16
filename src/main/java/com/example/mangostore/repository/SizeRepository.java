package com.example.mangostore.repository;

import com.example.mangostore.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "select * from size where status= 1 order by id desc", nativeQuery = true)
    List<Size> getAllSizeByStatus1();

    @Query(value = "select * from size where status= 0 order by id desc", nativeQuery = true)
    List<Size> getAllSizeByStatus0();

    @Query(value = "select * from size where name_size like %:searchSize% and status= 1", nativeQuery = true)
    List<Size> searchSize(@Param("searchSize") String searchSize);

    @Query(value = "select * from size where name_size= :nameSize and status= 1", nativeQuery = true)
    Size findByName(@Param("nameSize") String nameSize);
}
