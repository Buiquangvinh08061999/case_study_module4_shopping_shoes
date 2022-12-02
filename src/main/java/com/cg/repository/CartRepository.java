package com.cg.repository;

import com.cg.model.Cart;
import com.cg.model.dto.CartInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /*Hàm này chứa thông tin của thằng Cart (ta chỉ đổi tên qua cartInfoDTO)*/
    /*Phần này kiểm tra tất cả các trường ở(api/carts/add)ta tìm được userId ở phía trên rồi,ta truyền userId vào hàm để kiểm tra có tồn tại không?, làm ở phần add_cart(thêm vào giỏ hàng)*/
    @Query("SELECT NEW com.cg.model.dto.CartInfoDTO(" +
                "c.id, " +
                "c.grandTotal, " +
                "c.user" +
            ") " +
            "FROM Cart AS c " +
            "WHERE c.user.id = ?1 "
    )
    Optional<CartInfoDTO> findCartInfoDTOByUserId(Long id);



    /*Làm hàm đếm count*/
    @Query("SELECT NEW com.cg.model.dto.CartInfoDTO(" +
                "c.id, " +
                "c.grandTotal, " +
                "c.user" +
            ") " +
            "FROM Cart AS c " +
            "WHERE c.user.id = ?1"
    )
    List<CartInfoDTO> findCartIFDTOByUserId(Long id);
}
