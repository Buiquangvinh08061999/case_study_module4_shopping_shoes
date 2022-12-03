package com.cg.model.dto;

import com.cg.model.Cart;
import com.cg.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)

public class CartInfoDTO {

    /*các trường chính trong thằng này là carts gốc ở  trên database , nó tương tác phần Cart ở model, trả về các giá trị return Cart tương tác với nó*/
    private Long id;

    private String grandTotal;

    private UserDTO user;

    public CartInfoDTO(Long id, BigDecimal grandTotal,User user) {
        this.id = id;
        this.grandTotal = grandTotal.toString();
        this.user = user.toUserDTO();
    }

    public Cart toCart() {
        return new Cart()
                .setId(id)
                .setGrandTotal(new BigDecimal(grandTotal))
                .setUser(user.toUser());
    }

}
