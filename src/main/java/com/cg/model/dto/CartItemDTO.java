package com.cg.model.dto;

import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CartItemDTO {

    private Long id;

    private String title;

    private String price;

    private String quantity;

    private String totalPrice;

    private String urlImage;/*trường mới, hiện ảnh*/

    private ProductDTO product;

    private CartInfoDTO cart;  /*CartInFo chính là các trường chính của cart, ta chỉ đổi tên thành CartInFo*/



    public CartItemDTO(Long id, String title, BigDecimal price, int quantity, BigDecimal totalPrice, String urlImage, Product product, Cart cart) {
        this.id = id;
        this.title = title;
        this.price = price.toString();
        this.quantity = String.valueOf(quantity);
        this.totalPrice = totalPrice.toString();
        this.urlImage = urlImage;
        this.product = product.toProductDTO();
        this.cart = cart.toCartInfoDTO();
    }

    public CartItem toCartItem(){
        return new CartItem()
                .setId(id)
                .setTitle(title)
                .setPrice(new BigDecimal(price))
                .setQuantity(Integer.parseInt(quantity))
                .setTotalPrice(new BigDecimal(totalPrice))
                .setUrlImage(urlImage)
                .setCart(cart.toCart())
                .setProduct(product.toProduct());
    }
}
