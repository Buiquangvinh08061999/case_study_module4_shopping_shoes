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


    /*Tìm tất cả thông tin của thằng cart, theo userId truyền vào, Được dùng trong CartRest */
    @Override
    public Optional<CartInfoDTO> findCartInfoDTOByUserId(long id) {
        return cartRepository.findCartInfoDTOByUserId(id);
    }

    /*Đây là phần thêm mới cart, truyền cart,và cartItem vào, lưu đối tượng cart, và set cho cartItem.setCart lại*/
    /*Thằng cart là con của cartItem, Nên ta phải gọi cartRepo vào đây để lưu nó lại, và trong cartItem.setCart, truyền thằng cart được lưu vào*/
    /*Được tạo ra khi giỏ hàng đang trống, nó sẽ lưu thông tin các trường product, đẩy vào trong cart, cartItem, và save lại*/
    @Override
    public CartItem addNewCart(Cart cart, CartItem cartItem) {

        Cart cartNew = cartRepository.save(cart);

        cartItem.setCart(cartNew);

        return cartItemRepository.save(cartItem);
    }

    /*Lưu lại các phần cartItem, có các trường quan trọng của product, được sử dụng ở cartRest(add),Dùng cho giỏ hàng sản phẩm đã tồn tại, Và ta thêm sản phẩm mới thành công*/
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

    /*Xóa cartItem , nhưng lưu lại phần cart trống, trả lại giá tiền total(span) ở giỏ hàng*/
    @Override
    public CartInfoDTO doRemoveCartItem(Cart cart, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);

        Cart cartNew = cartRepository.save(cart);

        return cartNew.toCartInfoDTO();
    }


    /*Hàm count*/
    @Override
    public List<CartInfoDTO> findCartIFDTOByUserId(Long id) {
        return cartRepository.findCartIFDTOByUserId(id);
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
