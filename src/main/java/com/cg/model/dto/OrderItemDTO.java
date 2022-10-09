package com.cg.model.dto;

import com.cg.model.Order;
import com.cg.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Accessors(chain = true)
public class OrderItemDTO {

    private Long id;
    private String title;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;

    private String urlImage; /*trường mới*/

    private OrderDTO order;


    public OrderItemDTO(Long id, String title, BigDecimal price, int quantity, BigDecimal totalPrice,String urlImage, Order order) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.urlImage = urlImage;
        this.order = order.toOrderDTO();
    }



}
