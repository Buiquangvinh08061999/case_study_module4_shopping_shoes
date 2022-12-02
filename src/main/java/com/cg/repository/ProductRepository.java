package com.cg.repository;

import com.cg.model.Product;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage, " +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category" +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false "
    )
    List<ProductDTO> findAllProductDTO();

    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage, " +
                "p.name, " +
                "p.price, " +
                "p.describe, " +
                "p.category" +
            " ) " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "AND CONCAT(" +
                "p.id, " +
                "p.urlImage, " +
                "p.name, " +
                "p.price, " +
                "p.describe, " +
                "p.category.title " +
            ") LIKE ?1")

    List<ProductDTO> search(String keyword);


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage, " +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category" +
            ") " +
            "FROM Product AS p "+
            "WHERE p.id = ?1 "
    )
    Optional<ProductDTO> findProductDTOById(Long id);


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.name ASC")
    List<ProductDTO> findAllSortASCNameProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id," +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.name DESC"
    )
    List<ProductDTO> findAllSortDESCNameProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.id ASC"
    )
    List<ProductDTO> findAllSortASCIdProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.id DESC"
    )
    List<ProductDTO> findAllSortDESCIdProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity," +
                " p.describe, " +
                "p.createdAt," +
                " p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.price ASC"
    )
    List<ProductDTO> findAllSortASCPriceProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category" +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false " +
            "ORDER BY p.price DESC "
    )
    List<ProductDTO> findAllSortDESCPriceProductDTO();


    @Query("SELECT NEW com.cg.model.dto.CountDTO (" +
                "COUNT(p.id) " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false")
    CountDTO findAllCount();


    Boolean existsByName(String name);
    Boolean existsByNameAndIdIsNot(String name, Long id);/*dùng cho kiểm tra update*/


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage, " +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category" +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = true"
    )
    List<ProductDTO> findAllHistoryProductDTO();



    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.price BETWEEN 50000 AND 100000 " +
            "AND p.deleted = false"
    )
    List<ProductDTO> findAllBetWeenPriceProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt," +
                " p.updatedAt," +
                " p.category " +
            ")" +
            "FROM Product AS p " +
            "WHERE p.price BETWEEN 100000 AND 200000 " +
            "AND p.deleted = false"
    )
    List<ProductDTO> findAllBetWeenPrice100_200ProductDTO();


    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name," +
                " p.price, " +
                "p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.price BETWEEN 200000 AND 300000 " +
            "AND p.deleted = false"
    )
    List<ProductDTO> findAllBetWeenPrice200_300ProductDTO();



    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
                "p.id, " +
                "p.urlImage," +
                "p.name, " +
                "p.price," +
                " p.quantity, " +
                "p.describe, " +
                "p.createdAt, " +
                "p.updatedAt, " +
                "p.category " +
            ") " +
            "FROM Product AS p " +
            "WHERE p.price BETWEEN 300000 AND 500000 " +
            "AND p.deleted = false")
    List<ProductDTO> findAllBetWeenPrice300_500ProductDTO();


















}
