package com.cg.repository;

import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //Kiểm tra tên giày có bị trùng không
    Boolean existsByName(String name);



    //Hiển thị tất cả thông tin product ra
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
            "p.id, " +
            "p.urlImage, " +
            "p.name, " +
            "p.price, " +
            "p.quantity, " +
            "p.describe, " +
            "p.createdAt, " +
            "p.updatedAt, " +
            "p.category) " +
            "FROM Product AS p " +
            "WHERE p.deleted = false")
    List<ProductDTO> findAllProductDTO();



    //search tất cả thông tin ra
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
            "p.id, " +
            "p.urlImage, " +
            "p.name, " +
            "p.price, " +
            "p.describe, " +
            "p.category) " +
            "FROM Product AS p " +
            "JOIN Category As c " +
            "ON c.id = p.category.id " +
            "WHERE p.deleted = false AND CONCAT(" +
            "p.id, " +
            "p.urlImage, " +
            "p.name, " +
            "p.price, " +
            "p.describe, " +
            "c.title) LIKE %?1% ")

    List<ProductDTO> search(String keywork);

}
