package com.cg.repository;

import com.cg.model.CartItem;
import com.cg.model.dto.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /*Phần này kiểm tra ở ( api/carts/add )*/
    /*Trong phần cartItem có chứa trường cart(id) và product(id), ta phải truyền 2 giá trị đó vào để kiểm tra có tồn tại không*/
    /*Lấy tất cả thông tin của cartItem(có chứa cart.id và product.id ( ta sử dụng hàm này và truyền 2 tham số id của 2 trường (kiểm tra điều kiện)*/
    @Query("SELECT NEW com.cg.model.dto.CartItemDTO(" +
            "ci.id, " +
            "ci.title, " +
            "ci.price, " +
            "ci.quantity, " +
            "ci.totalPrice, ci.urlImage ," +
            "ci.product," +
            "ci.cart) " +
            "FROM CartItem AS ci " +
            "WHERE ci.cart.id = ?1 " +
            "AND ci.product.id = ?2 ")
    Optional<CartItemDTO> findCartItemDTOByCartIdAndProductId(Long cartId, Long productId);


    /*Lấy tất cả thông tin của cartItem(có chứa cart.id( ta sử dụng hàm này và truyền  tham số id của cart_id (kiểm tra điều kiện)*/
    @Query("SELECT NEW com.cg.model.dto.CartItemDTO(" +
            "ci.id, " +
            "ci.title, " +
            "ci.price, " +
            "ci.quantity, " +
            "ci.totalPrice, ci.urlImage, " +
            "ci.product," +
            "ci.cart) " +
            "FROM CartItem AS ci " +
            "WHERE ci.cart.id = ?1 ")
    List<CartItemDTO> findCartItemDTOByCartId(Long cartId);

    /*Phần này kiểm tra ở OrderServiceImpl có sử dụng*/

    /*Lấy tất cả các trường của CartItem, theo cart_id có tồn tại không, sử dụng ở phần doCreateOder, truyền cartInfo.getId vào, nếu có lấy tất cả các trường.v.v của cartItem, đẩy qua orderItem*/
    @Query("SELECT ci " +
            "FROM CartItem AS ci " +
            "WHERE ci.cart.id = ?1")
    List<CartItem> findAllCartItemByCart(Long cartId);
}
