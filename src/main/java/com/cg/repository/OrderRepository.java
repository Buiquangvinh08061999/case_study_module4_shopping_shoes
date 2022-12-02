package com.cg.repository;

import com.cg.model.Order;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.OrderDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    /*o.user, lay tat ca thong tin cua truong user ra, nó là thằng con của order, GetAPI, lấy tất cả thông tin order ra để hiển thị trong listOrderADMIN*/
    @Query("SELECT NEW com.cg.model.dto.OrderDTO (" +
                "o.id, " +
                "o.grandTotal, " +
                "o.createdAt , " +
                "o.user," +
                " o.orderStatus" +
            ") " +
            "FROM Order AS o "
    )
    List<OrderDTO> findAllOrderDTO();


    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id) " +
            ") " +
            "FROM Order AS o"
    )
    CountDTO findAllCount();


    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id) " +
            ") " +
            "FROM Order AS o " +
            "WHERE o.orderStatus.id = 1"
    )
    CountDTO findAllCountOrderStatus1();


    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id) " +
            ") " +
            "FROM Order AS o " +
            "WHERE o.orderStatus.id = 2"
    )
    CountDTO findAllCountOrderStatus2();

    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id)" +
            " ) " +
            "FROM Order AS o" +
            " WHERE o.orderStatus.id = 3"
    )
    CountDTO findAllCountOrderStatus3();



    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id) " +
            ") " +
            "FROM Order AS o " +
            "WHERE o.orderStatus.id = 4"
    )
    CountDTO findAllCountOrderStatus4();
}
