package com.cg.service.order;

import com.cg.model.Order;
import com.cg.model.OrderItem;
import com.cg.model.dto.CartInfoDTO;

import com.cg.model.dto.CountDTO;
import com.cg.model.dto.OrderDTO;
import com.cg.model.dto.OrderItemDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface OrderService extends IGeneralService<Order> {

    /*Anh Phước chạy cái doCreaterOder này thôi nhé, Phần dưới của trang admin làm đang bị lỗi chưa chạy*/
    Order doCreateOrder(OrderDTO orderDTO, CartInfoDTO cartInfoDTO);



    

    /*đã sử dùng được, lấy các trường oderItem(id), oder_id, user_id*/
    List<OrderItemDTO> findAllOrderItemDTO();

    /*tìm tất cả các trường của orderItem dựa theo orderId, ta đã lấy đc orderId ở phía dưới, thông qua data-th:order.id của nó, trong nút kích vào phía để hiện thị thông tin đơn hàng*/
    List<OrderItemDTO> findAllOrderItemDTOByOrderId(Long id);


    List<OrderDTO> findAllOrderDTO();/*Lay tất cả các trường order, bao gồm cả user nằm trong order*/

    /*Đếm tổng số lượng đơn hàng có trong order ra */
    CountDTO findAllCount();
}
