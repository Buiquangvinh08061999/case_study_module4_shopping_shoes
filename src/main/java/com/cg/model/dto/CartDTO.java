package com.cg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class CartDTO implements Validator {


    private String userId;

    private String productId;

    private String quantity;


    @Override
    public boolean supports(Class<?> aClass) {
        return CartDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CartDTO cartDTO = (CartDTO) target;

        String checkUserID = cartDTO.getUserId();
        String checkProductId = cartDTO.getProductId();
        String checkQuantity= cartDTO.getQuantity();

        if((checkUserID.trim().isEmpty())){
            errors.rejectValue("userId", "userId rỗng","Vui Lòng Cung Cấp Id Người Dùng");
            return;
        }
    }
}
