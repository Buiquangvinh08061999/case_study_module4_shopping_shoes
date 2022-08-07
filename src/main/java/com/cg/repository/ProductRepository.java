package com.cg.repository;

import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsByName(String name);

    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage, p.name, p.price, p.quantity, p.describe, p.category) FROM Product AS p")
    List<ProductDTO> findAllProductDTO();
}
