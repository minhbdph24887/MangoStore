package com.example.mangostore.repository;

import com.example.mangostore.entity.PriceRange;
import com.example.mangostore.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query(value = "select * from product_detail where status= 1 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus1();

    @Query(value = "select * from product_detail where status= 0 order by id desc", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByStatus0();

    @Query(value = "select * from product_detail where id= :idProductDetail and status= 1", nativeQuery = true)
    List<ProductDetail> findProductDetailById(@Param("idProductDetail") Long idProductDetail);

    @Query(value = "select product_detail.* from product_detail inner join (select id_product, max(id) as MaxId from product_detail group by id_product) temp on product_detail.id_product = temp.id_product and product_detail.id = temp.MaxId inner join product on product_detail.id_product = product.id", nativeQuery = true)
    List<ProductDetail> getAllProductDetailByIdProduct();

    @Query(value = "select min(pd.price) as priceMin, max(pd.price) as priceMax from product_detail pd where pd.id_product = :idProduct", nativeQuery = true)
    List<Object[]> findAllPriceByIdProduct(@Param("idProduct") Long idProduct);
}
