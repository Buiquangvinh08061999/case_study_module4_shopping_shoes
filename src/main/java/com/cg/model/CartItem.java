package com.cg.model;

import com.cg.model.dto.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
@Accessors(chain = true)

public class CartItem extends BaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Digits(integer = 12, fraction = 0)
    private BigDecimal price;

    private int quantity;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "total_price")
    private BigDecimal totalPrice;


    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public CartItemDTO toCartItemDTO(){
        return new CartItemDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price.toString())
                .setQuantity(String.valueOf(quantity))
                .setTotalPrice(totalPrice.toString())
                .setCart(cart.toCartInfoDTO())
                .setProduct(product.toProductDTO());
    }

}
