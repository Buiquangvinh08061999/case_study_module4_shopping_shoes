package com.cg.service.order;

import com.cg.model.CartItem;
import com.cg.model.Order;
import com.cg.model.OrderItem;
import com.cg.model.dto.*;

import com.cg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Order getById(Long id) {
        return null;
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Order doCreateOrder(OrderDTO orderDTO, CartInfoDTO cartInfoDTO) {

        Order order = new Order();
        order.setId(0L);
        order.setGrandTotal(new BigDecimal(cartInfoDTO.getGrandTotal()));/*lấy tổng tiền tính được ở phần cartGrandTotal, truyền cho thằng order*/
        order.setDeliveryDate("0");

        order.setUser(cartInfoDTO.getUser().toUser()); /*lấy 3 thông tin của cart ra, gồm id, tổng tiền, user id, set vào order.setUserj*/

        Order orderNew = orderRepository.save(order); /*Lưu lại thông tin của cartInfo(giỏ hàng) sang hết cho order*/

        /*Chuẩn bị set các trường của cartItem sang orderItem*/
        OrderItem orderItem = new OrderItem();

        /*tìm tất cả thông tin trong cartItem,có cart_id = 3, sẽ duyệt hết qua thằng 3, và lấy hết thong tin từ điều kiện cartItem = 3 ra(Không đụng chạm đến và k duyệt qua những thằng có cart_id khác nó)*/
        List<CartItem> cartItemList = cartItemRepository.findAllCartItemByCart(cartInfoDTO.getId());
        for (CartItem cartItem : cartItemList){
            orderItem.setId(0L);
            orderItem.setTitle(cartItem.getTitle());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setUrlImage(cartItem.getUrlImage());/*lấy thêm ảnh để hiển thị trong listOrder*/

            orderItem.setOrder(orderNew);/*truyền tất cả thông tin vào thằng orderNew vừa save lại vào,(tại bảng orderItem có chứa order(nên ta truyền dữ liệu vào)*/

            orderItemRepository.save(orderItem);/*khi lấy hết dữ liệu sang orderItem, thì ta lưu lại vào repo(và xóa nó đi ở bản cartItem(xóa tất cả các trường, bao gồm cả cart_id nằm ở đó, sang hết cho orderItem),*/

            cartItemRepository.deleteById(cartItem.getId());/*Thành công (tiến hành xóa cartItem, và cart(cartInfo)*/
        }

        cartRepository.deleteById(cartInfoDTO.getId());

        return orderNew;
    }

    @Override
    public List<OrderItemDTO> findAllOrderItemDTO() {
        return orderItemRepository.findAllOrderItemDTO();
    }

    @Override
    public List<OrderItemDTO> findAllOrderItemDTOByOrderId(Long id) {
        return orderItemRepository.findAllOrderItemDTOByOrderId(id);
    }

    /**Lấy tất cả thông tin của thằng order ra(bao gồm id, user_id(chua tat ca cac trường user ra) */
    @Override
    public List<OrderDTO> findAllOrderDTO() {
        return orderRepository.findAllOrderDTO();
    }


    @Override
    public CountDTO findAllCount() {
        return orderRepository.findAllCount();
    }
}

