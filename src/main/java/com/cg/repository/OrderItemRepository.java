package com.cg.repository;

import com.cg.model.OrderItem;

import com.cg.model.dto.CountDTO;
import com.cg.model.dto.OrderItemDTO;
import com.cg.model.dto.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    @Query("SELECT NEW com.cg.model.dto.OrderItemDTO(" +
                "o.id, " +
                "o.title," +
                "o.price, " +
                "o.quantity, " +
                "o.totalPrice," +
                "o.urlImage, " +
                "o.order" +
            ") " +
            "FROM OrderItem AS o ")
    List<OrderItemDTO> findAllOrderItemDTO();



    /*phần o.order, ta chỉ cần trỏ đến nó thì sẽ được tất cả giá trị, từ oderid, và sổ ra userid chứa trong nó, trong userid chưa role với location .*/
    @Query("SELECT NEW com.cg.model.dto.OrderItemDTO(" +
                "o.id, " +
                "o.title," +
                "o.price, " +
                "o.quantity, " +
                "o.totalPrice, o.urlImage, " +
                "o.order" +
            ") " +
            "FROM OrderItem AS o " +
            "WHERE o.order.id = ?1 "
    )
    List<OrderItemDTO> findAllOrderItemDTOByOrderId(Long id);



    /*Đếm tổng số lượng sản phẩm, đang tốn tại(Get api/products/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(o.id) " +
            ")" +
            "FROM OrderItem AS o " +
            "WHERE o.order.id = ?1"
    )
    CountDTO findAllCountOrderItemByOrderId(long id);
}
