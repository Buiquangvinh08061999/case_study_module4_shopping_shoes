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

public class OrderDTO implements Validator  {

    private String userId;

    private Long id;

    private BigDecimal grandTotal;

    private UserDTO user;

    private OrderStatusDTO orderStatus;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;


    public Order toOrder(){
        return (Order) new Order()
                .setId(id)
                .setGrandTotal(grandTotal)
                .setUser(user.toUser())
                .setOrderStatus(orderStatus.toOrderStatus())
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt);
    }


    public OrderDTO(Long id, BigDecimal grandTotal,Date createdAt, User user, OrderStatus orderStatus) {
        this.id = id;
        this.grandTotal = grandTotal;
        this.createdAt = createdAt;
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
