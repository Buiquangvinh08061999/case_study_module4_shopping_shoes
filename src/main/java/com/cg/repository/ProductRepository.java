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



    /*Hiển thị tất cả thông tin Product ra với điều kiện nó còn tộn tại(nếu p.deleted = false, = true thì nó đã xóa)*/
    /*Phần này viết ở /api/products, (GET), chỉ cần vào ajax viết hàm, dùng foreach để duyệt hết và gọi lại api như trên, sẽ có tất cả thông tin, ta chỉ truyền vào, và vẽ lại phần body*/
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
            "WHERE p.deleted = false ")
    List<ProductDTO> findAllProductDTO();

    /*Phần này viết ở /api/products/search (POST) */
    /*Search tất cả thông tin ra, JOIN 2 bản lại với nhau để tìm kiếm danh mục phía bảng khoái ngoại Category, nhưng ta tạo title của Category ở trong Product(để có thể search ra danh mục)*/
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


    //Hiển thị tất cả thông tin product ra dựa theo p.id của nó, Lấy product.id có ở hàm findProductDTO,
    //Dùng trong phần (api/carts/add) add cart, tương tự với user, tìm tất cả theo user.id, Ở phần list Order, ta đã có tìm được id(user) và id(product), ta chỉ cần truyền vào để hiển thị tất cả thông tin của 2 trường này
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
            "FROM Product AS p "+
            "WHERE p.id = ?1 ")
    Optional<ProductDTO> findProductDTOById(Long id);


    /*Sắp xếp tên theo thứ tự tăng dần dựa theo tên(Get api/products/sortASCName)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.name ASC")
    List<ProductDTO> findAllSortASCNameProductDTO();

    /*Sắp xếp tên theo thứ tự tăng dần dựa theo tên(Get api/products/sortASCName)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.name DESC")
    List<ProductDTO> findAllSortDESCNameProductDTO();

    /*Sắp xếp id theo thứ tự tăng dần dựa theo id(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.id ASC")
    List<ProductDTO> findAllSortASCIdProductDTO();

    /*Sắp xếp id theo thứ tự giảm dần dựa theo id(Get api/products/sortDESCId)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.id DESC")
    List<ProductDTO> findAllSortDESCIdProductDTO();

    /*Sắp xếp price theo thứ tự tăng dần dựa theo price(Get api/products/sortASCPrice)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.price ASC")
    List<ProductDTO> findAllSortASCPriceProductDTO();

    /*Sắp xếp price theo thứ tự giảm dần dựa theo price(Get api/products/sortDESCPrice)*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.deleted = false ORDER BY p.price DESC ")
    List<ProductDTO> findAllSortDESCPriceProductDTO();

    /*Đếm tổng số lượng sản phẩm, đang tốn tại(Get api/products/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(p.id) ) FROM Product AS p WHERE p.deleted = false")
    CountDTO findAllCount();



//    @Query(value = "SELECT COUNT(*) FROM ProductDTO", nativeQuery = true)
//    void findAllCountProductDTO();



    /*Kiểm tra tên giày có bị trùng không*/
    Boolean existsByName(String name);/*dùng cho kiểm tra khi create vào*/

    /*Cập nhật cho trùng tên của hiện tại của nó, ID của nó , nhưng không trùng tên của thằng khác */
    Boolean existsByNameAndIdIsNot(String name, Long id);/*dùng cho kiểm tra update*/



    /*Hiển thị tất cả thông tin Product ra với điều kiện nó đã bị xóa mềm(nếu p.deleted = 1  (true))*/
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
            "WHERE p.deleted = true")
    List<ProductDTO> findAllHistoryProductDTO();




    /*Những phương thức này viết trong api/products, nhưng được sử dụng trong giao diện người dùng, ListOrder*/
    /*Lấy ra số tiền(price) trong khoảng 50k đến 100k với điều kiện deleted =0, tức là còn tồn tại*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.price BETWEEN 50000 AND 100000 AND p.deleted = false")
    List<ProductDTO> findAllBetWeenPriceProductDTO();

    /*Lấy ra số tiền(price) trong khoảng 100k đến 200k với điều kiện deleted =0, tức là còn tồn tại*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.price BETWEEN 100000 AND 200000 AND p.deleted = false")
    List<ProductDTO> findAllBetWeenPrice100_200ProductDTO();


    /*Lấy ra số tiền(price) trong khoảng 200k đến 300k với điều kiện deleted =0, tức là còn tồn tại*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.price BETWEEN 200000 AND 300000 AND p.deleted = false")
    List<ProductDTO> findAllBetWeenPrice200_300ProductDTO();


    /*Lấy ra số tiền(price) trong khoảng 300k đến 500k với điều kiện deleted =0, tức là còn tồn tại*/
    @Query("SELECT NEW com.cg.model.dto.ProductDTO(p.id, p.urlImage,p.name, p.price, p.quantity, p.describe, p.createdAt, p.updatedAt, p.category ) FROM Product AS p WHERE p.price BETWEEN 300000 AND 500000 AND p.deleted = false")
    List<ProductDTO> findAllBetWeenPrice300_500ProductDTO();






    /*Câu lệnh hay, nếu muốn * ra tất cả thì ta phải dùng  nativeQuery = true(Ở trên chúng ta đang dùng HQL, muốn qua SQL thuần thì làm cách dưới*/
//    @Query(value = "SELECT  * FROM products  3 ",   nativeQuery = true )
//    List<Product> findByName(@Param("name") String name);




    //Dùng để test phân trang
//    @Query("SELECT NEW com.cg.model.dto.ProductDTO(" +
//            "p.id, " +
//            "p.urlImage, " +
//            "p.name, " +
//            "p.price, " +
//            "p.quantity, " +
//            "p.describe, " +
//            "p.createdAt, " +
//            "p.updatedAt, " +
//            "p.category) " +
//            "FROM Product AS p " +
//            "WHERE p.deleted = false ")
//    Page<ProductDTO> findAllPage(Pageable pageable);









}
