package com.example.mangostore.repository;

import com.example.mangostore.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(value = "select * from material where status= 1 order by id desc", nativeQuery = true)
    Page<Material> getAllMaterialByStatus1(Pageable pageable);

    @Query(value = "select * from material where status= 0 order by id desc", nativeQuery = true)
    Page<Material> getAllMaterialByStatus0(Pageable pageable);

    @Query(value = "select * from material where name_material like %:searchMaterial%", nativeQuery = true)
    Page<Material> searchMaterial(Pageable pageable, @Param("searchMaterial") String searchMaterial);
}
