package com.cg.service.CartItem;

import com.cg.model.CartItem;
import com.cg.model.dto.CartItemDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface CartItemService extends IGeneralService<CartItem> {

    Optional<CartItemDTO> findCartItemDTOByCartIdAndProductId(long cartId, long productId);


    /*tìm tất cả các trường CartItem dựa vào cart(id) được truyền vào, để lấy các trường đó ra*/
    List<CartItemDTO> findCartItemDTOByCartId(Long cartId);


}
