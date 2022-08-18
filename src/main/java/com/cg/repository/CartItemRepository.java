package com.cg.repository;

import com.cg.model.CartItem;
import com.cg.model.dto.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT NEW com.cg.model.dto.CartItemDTO (ci.id, ci.title, ci.price, ci.quantity, ci.totalPrice, ci.product, ci.cart) FROM CartItem AS ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    Optional<CartItemDTO> findCartItemDTOByCartIdAndProductId(Long cartId, Long productId);

    @Query("SELECT NEW com.cg.model.dto.CartItemDTO (ci.id, ci.title, ci.price, ci.quantity, ci.totalPrice, ci.product, ci.cart) FROM CartItem AS ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    List<CartItemDTO> findCartItemDTOByCartId(Long cartId);

    @Query("SELECT ci FROM CartItem AS ci WHERE ci.cart.id = ?1")
    List<CartItemDTO> findAllCartItemByCart(Long cartId);
}
