package com.cg.repository;

import com.cg.model.Order;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.OrderDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

//
//    @Query("SELECT NEW com.cg.model.Order(" +
//            "SUM(o.grandTotal) " +
//            ") " +
//            "FROM Order AS o")
//
//    List<Order> sum();

    /*o.user, lay tat ca thong tin cua truong user ra, nó là thằng con của order, GetAPI, lấy tất cả thông tin order ra để hiển thị trong listOrderADMIN*/
    @Query("SELECT NEW com.cg.model.dto.OrderDTO (o.id, o.grandTotal, o.orderDate , o.user, o.orderStatus) FROM Order AS o")
    List<OrderDTO> findAllOrderDTO();




    /*Đếm tổng số lượng đơn hàng, đang tồn tại(Get api/orders/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(o.id) ) FROM Order AS o")
    CountDTO findAllCount();



    /*Đếm tổng số lượng đang chờ duyệt,(Get api/orders/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(o.id) ) FROM Order AS o WHERE o.orderStatus.id = 1")
    CountDTO findAllCountOrderStatus1();

    /*Đếm tổng số lượng đang giao hàng, đang tốn tại(Get api/orders/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(o.id) ) FROM Order AS o WHERE o.orderStatus.id = 2")
    CountDTO findAllCountOrderStatus2();

    /*Đếm tổng số lượng đang nhận hàng, đang tốn tại(Get api/orders/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(o.id) ) FROM Order AS o WHERE o.orderStatus.id = 3")
    CountDTO findAllCountOrderStatus3();

    /*Đếm tổng số lượng đã hủy đơn, đang tốn tại(Get api/orders/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(o.id) ) FROM Order AS o WHERE o.orderStatus.id = 4")
    CountDTO findAllCountOrderStatus4();
}
