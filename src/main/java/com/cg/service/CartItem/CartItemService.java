package com.cg.service.CartItem;

import com.cg.model.CartItem;
import com.cg.model.dto.CartItemDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface CartItemService extends IGeneralService<CartItem> {

    Optional<CartItemDTO> findCartItemDTOByCartIdAndProductId(long cartId, long productId);


    /*tìm tất cả các trường CartItem dựa vào cart(id) được truyền vào, để lấy các trường đó ra*/
    /*Bước đẩu xử lí, truyền tham số userId vào trước, /api/carts/{id}, kiếm tra điều kiện, tìm tất cả các trường cartInfo theo userId, tìm ra được rồi. thì ta có cartInfo.get.getId, chính là id truyền vào ở phần dưới, để số tất cả thông tin cartItem ra/*/
    List<CartItemDTO> findCartItemDTOByCartId(Long cartId);


}
