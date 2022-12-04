package com.cg.controller.rest;


import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Order;
import com.cg.model.OrderItem;
import com.cg.model.OrderStatus;
import com.cg.model.Product;
import com.cg.model.dto.*;
import com.cg.service.Cart.CartService;
import com.cg.service.CartItem.CartItemService;
import com.cg.service.OrderStatus.IOrderStatusService;
import com.cg.service.order.OrderService;
import com.cg.service.product.IProductService;
import com.cg.service.user.IUserService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private AppUtil appUtils;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IOrderStatusService orderStatusService;

    @GetMapping()/*Lay tat ca cac truong cua tk orderItem ra*/
    public ResponseEntity<?> getAllOrderItemDTO(){

        List<OrderItemDTO> orderItemDTO = orderService.findAllOrderItemDTO();

        return new ResponseEntity<>(orderItemDTO , HttpStatus.OK);

    }

    @GetMapping("/order")/*Lấy tất cả các trường orderId ra*/
    public ResponseEntity<?> getAllOrderDTO(){

        List<OrderDTO> orderDTO = orderService.findAllOrderDTO();

        return new ResponseEntity<>(orderDTO , HttpStatus.OK);

    }

    @GetMapping("orderItem/{id}") /*truyền vào orderId đã tìm ở tất cả các trường ở hàm trên vào trong phần tìm tất cả orderItem, theo orderId*/
    public ResponseEntity<?> getOrderItemDTOById(@PathVariable long id) {

        List<OrderItemDTO> orderItemDTO = orderService.findAllOrderItemDTOByOrderId(id);

        return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);

    }


    /*Đếm số lượng tổng đơn hàng*/
    @GetMapping("/count")
    public ResponseEntity<?> getAllCountOrderDTO() {
        CountDTO countDTO = orderService.findAllCount();

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }
    /*Đếm số lượng sản phẩm, để hiện thị thông tin chi tiết sản phẩm, có bao nhiêu đơn hàng*/
    @GetMapping("orderItem/orderId/{id}") /*truyền vào orderId đã tìm ở tất cả các trường ở hàm trên vào trong phần tìm tất cả orderItem, theo orderId*/
    public ResponseEntity<?> getCountOrderItemDTOByOrderId(@PathVariable long id) {

        CountDTO countDTO = orderService.findAllCountOrderItemByOrderId(id);

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }

    }

    /*Đếm số lượng trạng thái của từng orderStatus(có 4 trạng thái)*/
    @GetMapping("/count/orderStatus1")
    public ResponseEntity<?> getAllCountOrderStatus1() {
        CountDTO countDTO = orderService.findAllCountOrderStatus1();

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }
    @GetMapping("/count/orderStatus2")
    public ResponseEntity<?> getAllCountOrderStatus2() {
        CountDTO countDTO = orderService.findAllCountOrderStatus2();

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }
    @GetMapping("/count/orderStatus3")
    public ResponseEntity<?> getAllCountOrderStatus3() {
        CountDTO countDTO = orderService.findAllCountOrderStatus3();

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }
    @GetMapping("/count/orderStatus4")
    public ResponseEntity<?> getAllCountOrderStatus4() {
        CountDTO countDTO = orderService.findAllCountOrderStatus4();

        try {
            return new ResponseEntity<>(countDTO, HttpStatus.OK);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }


    @PostMapping("/add")
    public ResponseEntity<?> doAddOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult){
        new OrderDTO().validate(orderDTO , bindingResult);

        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<UserDTO> userDTOOptional = userService.findUserDTOById(Long.parseLong(orderDTO.getUserId()));
        if(!userDTOOptional.isPresent()){
            throw new ResourceNotFoundException("Không tìm thấy Id user");
        }

        /*Kiểm tra userId, trong trường cart(cartInfo có chứa trường userId, kiểm tra userId có tồn tại không)*/
        Long userId = userDTOOptional.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        Map<String , Object> result = new HashMap<>();
        String success;

        if(!cartInfoDTO.isPresent()){
            throw new ResourceNotFoundException("Giỏ hàng của bạn đang trống!");
        }

        /*Kiểm tra, trong CartItemDTO có trường cart_id( ta tiến hành kiếm tra xem cartId(cartInfoDTO) có tồn tại không */
        List<CartItemDTO> cartItemDTOList = cartItemService.findCartItemDTOByCartId(cartInfoDTO.get().getId());
        if(cartItemDTOList.isEmpty()){
            throw new ResourceNotFoundException("Người dùng chưa có sản phẩm trong giỏ hàng để đặt thanh toán");
        }else {

            try {
                orderService.doCreateOrder(orderDTO, cartInfoDTO.get());
                success = "Đặt hàng thành công";
                result.put("success", success);

                return new ResponseEntity<>(result, HttpStatus.CREATED);

            } catch (DataInputException e) {
                throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
            }
        }

    }

    @GetMapping("/{id}") /*truyền vào orderId đã tìm ra ở data-id:${order.id} truyền vào đây để lấy giá trị, phương thức hiển thị dữ liệu Edit theo id, ta chỉ lấy 1 trường orderStatus*/
    public ResponseEntity<?> getOrderStatusDTOById(@PathVariable long id) {

        Optional<Order> orderOptional = orderService.findById(id);

        if(!orderOptional.isPresent()){
            throw new ResourceNotFoundException("Id của order không tồn tại");

        }

        return new ResponseEntity<>(orderOptional.get().toOrderDTO(), HttpStatus.OK);

    }
    /*Cập nhật trạng thái đơn hàng, qua trạng thái khác có trong orderStatus, Cập nhật được nhiều trường, nhưng ở đây ta chỉ cập nhật 1 trường duy nhất đó là orderStatus*/
    @PutMapping("/update")
    public ResponseEntity<?> doUpdateOrderStatus(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<OrderStatus> orderStatusId = orderStatusService.findById(orderDTO.getOrderStatus().getId());
        if(!orderStatusId.isPresent()){
            throw new DataInputException("ID orderStatus không tồn tại!");
        }

        try {
            Order updateOrder = orderService.save(orderDTO.toOrder());

            return new ResponseEntity<>(updateOrder.toOrderDTO(),  HttpStatus.CREATED);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin không hợp lệ ");
        }
    }



}
