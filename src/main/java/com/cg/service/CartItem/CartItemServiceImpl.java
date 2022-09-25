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

    /*Xử lí tìm cardId đã tồn tại chưa*/
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
