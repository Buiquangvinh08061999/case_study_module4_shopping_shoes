package com.cg.model.dto;

import com.cg.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)

public class OrderDTO  implements Validator  {

    private String userId;


    private Long id;
    private BigDecimal grandTotal;


    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date orderDate; /*Lấy ra ngày tạo, khi cập nhật lại cũng không ảnh hưởng*/


    private UserDTO user;

    private OrderStatusDTO orderStatus;



    public Order toOrder(){
        return new Order()
                .setId(id)
                .setGrandTotal(grandTotal)
                .setOrderDate(orderDate)
                .setUser(user.toUser())
                .setOrderStatus(orderStatus.toOrderStatus());
    }



    public OrderDTO(Long id, BigDecimal grandTotal,Date orderDate, User user, OrderStatus orderStatus) {
        this.id = id;
        this.grandTotal = grandTotal;
        this.orderDate = orderDate;
        this.user = user.toUserDTO();
        this.orderStatus = orderStatus.toOrderStatusDTO();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OrderDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderDTO orderDTO = (OrderDTO) target;

        String checkUserId = orderDTO.getUserId();
        if(checkUserId.trim().isEmpty()){
            errors.rejectValue("userId", "userId rỗng","Vui Lòng Cung Cấp Id Người Dùng");
            return;
        }
    }


}
