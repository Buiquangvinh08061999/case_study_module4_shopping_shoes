package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderStatusDTO {

    private Long id;
    private String title;

    private boolean deleted;

    public OrderStatus toOrderStatus() {
        return new OrderStatus()
                .setId(id)
                .setTitle(title);
    }

}
