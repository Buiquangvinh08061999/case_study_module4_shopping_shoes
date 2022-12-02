package com.cg.service.Cart;

import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.dto.CartInfoDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface CartService extends IGeneralService<Cart> {

    Optional<CartInfoDTO> findCartInfoDTOByUserId(long id);

    CartItem addNewCart(Cart cart, CartItem cartItem);

    CartItem addProductByCart(Cart cart, CartItem cartItem);

    /*Phương thức này dùng để cập nhật Tăng và Giảm số lượng trong phần Post(api/carts/increase và api/carts/reduce*/
    CartItem updateProductByCart(Cart cart, CartItem cartItem);

    /*Phần này thực hiện xóa cartItem ra*/
    CartInfoDTO doRemoveCartItem(Cart cart, Long cartItemId);





}
