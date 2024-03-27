package com.example.mangostore.repository;

import com.example.mangostore.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select pd.id, pd.images_product_detail, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.status\n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product) \n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id", nativeQuery = true)
    Page<ProductDetail> getAllProductDetailByIdProduct(Pageable pageable);

    @Query(value = "select min(pd.price) as priceMin, max(pd.price) as priceMax from product_detail pd where pd.id_product = :idProduct", nativeQuery = true)
    List<Object[]> findAllPriceByIdProduct(@Param("idProduct") Long idProduct);

    @Query(value = "select count(*) from product_detail where id_size = :idSize", nativeQuery = true)
    Integer countProductDetailBySize(@Param("idSize") Long idSize);

    @Query(value = "select count(*) from product_detail where id_color = :idColor", nativeQuery = true)
    Integer countProductDetailByColor(@Param("idColor") Long idColor);

    @Query(value = "select pd.id, pd.images_product_detail, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.status \n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product)\n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id order by pd.price asc", nativeQuery = true)
    Page<ProductDetail> sortProductDetailLowToHigh(Pageable pageable);

    @Query(value = "select pd.id, pd.images_product_detail, pd.id_product, pd.id_material, pd.id_size, pd.id_color, pd.id_origin, pd.id_category, pd.describe, pd.quantity,\n" +
            "pd.import_price, pd.price, pd.name_user_create, pd.name_user_update, pd.date_create, pd.date_update, pd.status \n" +
            "from product_detail pd inner join (select id_product, max(id) as MaxId from product_detail group by id_product)\n" +
            "temp on pd.id_product = temp.id_product and pd.id = temp.MaxId inner join product p on pd.id_product = p.id order by pd.price desc", nativeQuery = true)
    Page<ProductDetail> sortProductDetailHighToLow(Pageable pageable);

    @Query(value = "select * from product_detail where id_product= :idProduct and id_size= :idSize and id_color= :idColor", nativeQuery = true)
    ProductDetail getQuantityProductDetail(@Param("idProduct") Long idProduct, @Param("idSize") Long idSize, @Param("idColor") Long idColor);
}
