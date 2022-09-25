package com.cg.service.Cart;

import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.dto.CartInfoDTO;
import com.cg.repository.CartItemRepository;
import com.cg.repository.CartRepository;
import com.cg.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public Optional<CartInfoDTO> findCartInfoDTOByUserId(long id) {
        return cartRepository.findCartInfoDTOByUserId(id);
    }

    /*Tạo mới cartId, lưu lại cart*/
    @Override
    public CartItem addNewCart(Cart cart, CartItem cartItem) {

        Cart cartNew = cartRepository.save(cart);

        cartItem.setCart(cartNew);

        return cartItemRepository.save(cartItem);
    }

    /*Lưu lại phần cartItem, có các trường quan trọng của product*/
    @Override
    public CartItem addProductByCart(Cart cart, CartItem cartItem) {
        CartItem cartItemNew = cartItemRepository.save(cartItem);

        cartRepository.save(cart).toCartInfoDTO();

        return cartItemNew;
    }

    @Override
    public CartItem updateProductByCart(Cart cart, CartItem cartItem) {
        /*save thằng cart trước, để nó cập nhật tiền , rồi sau đó mới save cartItem sau*/
        cartRepository.save(cart).toCartInfoDTO();

        CartItem cartItemNew = cartItemRepository.save(cartItem);

        return cartItemNew;
    }

    @Override
    public CartInfoDTO doRemoveCartItem(Cart cart, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);

        Cart cartNew = cartRepository.save(cart);

        return cartNew.toCartInfoDTO();
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Cart getById(Long id) {
        return null;
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void remove(Long id) {

    }
}
