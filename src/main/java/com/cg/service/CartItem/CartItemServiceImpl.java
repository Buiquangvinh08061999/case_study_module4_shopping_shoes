package com.cg.service.CartItem;

import com.cg.model.CartItem;
import com.cg.model.dto.CartItemDTO;
import com.cg.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Optional<CartItemDTO> findCartItemDTOByCartIdAndProductId(long cartId, long productId) {
        return cartItemRepository.findCartItemDTOByCartIdAndProductId(cartId, productId);
    }

    /*Xử lí tìm cardId đã tồn tại chưa, và tìm tất cả thông tin của tất cả các trường, dựa theo cartId truyền vào */
    /*show hiển thị, khi kích vào sẽ sổ ra thông tin, bao gồm tất cả các trường của nó, bao gồm giá tiền, ảnh, tên..vv */
    /*Bước đẩu xử lí, truyền tham số userId vào trước, /api/carts/{id}, kiếm tra điều kiện, tìm tất cả các trường cartInfo theo userId, tìm ra được rồi. thì ta có cartInfo.get.getId, chính là id truyền vào ở phần dưới, để số tất cả thông tin cartItem ra/*/
    @Override
    public List<CartItemDTO> findCartItemDTOByCartId(Long cartId) {
        return cartItemRepository.findCartItemDTOByCartId(cartId);
    }

    @Override
    public List<CartItem> findAll() {
        return null;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public CartItem getById(Long id) {
        return null;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
