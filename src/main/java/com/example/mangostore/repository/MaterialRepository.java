package com.example.mangostore.repository;

import com.example.mangostore.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(value = "select * from material where status= 1 order by id desc", nativeQuery = true)
    List<Material> getAllMaterialByStatus1();

    @Query(value = "select * from material where status= 0 order by id desc", nativeQuery = true)
    List<Material> getAllMaterialByStatus0();

    @Query(value = "select * from material where name_material like %:searchMaterial and status= 1%", nativeQuery = true)
    List<Material> searchMaterial(@Param("searchMaterial") String searchMaterial);

    @Query(value = "select * from material where id= :idMaterial", nativeQuery = true)
    Material findByMa(@Param("idMaterial") Long material);
}
