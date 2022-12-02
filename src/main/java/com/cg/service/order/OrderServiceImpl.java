package com.cg.service.order;

import com.cg.model.CartItem;
import com.cg.model.Order;
import com.cg.model.OrderItem;
import com.cg.model.OrderStatus;
import com.cg.model.dto.*;

import com.cg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public List<Order> findAll() {
        return null;
    }

    /*trường này tìm theo id của Order, để sổ hết thông tin của nó ra, Dùng cho showUpdate và Detail*/
    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order getById(Long id) {
        return null;
    }

    @Override
    public Order save(Order order) {
        OrderStatus orderStatus = orderStatusRepository.save(order.getOrderStatus());

        order.setOrderStatus(orderStatus);

        return orderRepository.save(order);
    }

    @Override
    public void remove(Long id) {

    }



    @Override
    public Order doCreateOrder(OrderDTO orderDTO, CartInfoDTO cartInfoDTO) {

        Order order = new Order();
        OrderStatus orderStatus = new OrderStatus();
        order.setId(0L);
        order.setGrandTotal(new BigDecimal(cartInfoDTO.getGrandTotal()));/*lấy tổng tiền tính được ở phần cartGrandTotal ra, truyền cho thằng order.Grand_total*/

        order.setUser(cartInfoDTO.getUser().toUser()); /*lấy 3 thông tin của cart ra, gồm id, tổng tiền, user id, set vào order.setUserj*/

        order.setOrderStatus(orderStatus.setId(1L));/*Mặc định trạng thái của nó là id =1("Đang chờ duyệt")*/

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

            orderItem.setOrder(orderNew);/*truyền tất cả thông tin vào thằng orderNew(tức là order) vừa save lại vào,(tại bảng orderItem có chứa orderId(nên ta truyền dữ liệu vào, sẽ có tất cả thông tin order nằm trong bảng chính OrderItem)*/

            orderItemRepository.save(orderItem);/*khi lấy hết dữ liệu sang orderItem, thì ta lưu lại vào repo(và xóa nó đi ở bản cartItem(xóa tất cả các trường, bao gồm cả cart_id nằm ở đó, sang hết cho orderItem),*/

            cartItemRepository.deleteById(cartItem.getId());/*Thành công (tiến hành xóa cartItem, và cart(cartInfo),  xóa nhiều cartItem nên ở trong vòng lặp*/
        }

        cartRepository.deleteById(cartInfoDTO.getId());/*Thành công xóa luôn phần cart, truyền vào id của nó để xóa, xóa ít nên để ngoài vòng lặp*/

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


    @Override
    public CountDTO findAllCountOrderStatus1() {
        return orderRepository.findAllCountOrderStatus1();
    }

    @Override
    public CountDTO findAllCountOrderStatus2() {
        return orderRepository.findAllCountOrderStatus2();
    }

    @Override
    public CountDTO findAllCountOrderStatus3() {
        return orderRepository.findAllCountOrderStatus3();
    }

    @Override
    public CountDTO findAllCountOrderStatus4() {
        return orderRepository.findAllCountOrderStatus4();
    }


    @Override
    public CountDTO findAllCountOrderItemByOrderId(long id) {
        return orderItemRepository.findAllCountOrderItemByOrderId(id);
    }
}

