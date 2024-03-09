package com.example.mangostore.repository;

import com.example.mangostore.entity.AddressClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressClientRepository extends JpaRepository<AddressClient, Long> {
    @Query(value = "select * from address_client where status= 1 order by id desc", nativeQuery = true)
    List<AddressClient> getAllAddressClientByStatus1();

    @Query(value = "select * from address_client where status= 0 order by id desc", nativeQuery = true)
    List<AddressClient> getAllAddressClientByStatus0();

    @Query(value = "select * from address_client where name_client like %:searchNameClient%", nativeQuery = true)
    List<AddressClient> searchNameClient(@Param("searchNameClient") String searchClient);
}
