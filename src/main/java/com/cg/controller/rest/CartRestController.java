package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.dto.*;
import com.cg.repository.CartItemRepository;
import com.cg.service.Cart.CartService;
import com.cg.service.CartItem.CartItemService;
import com.cg.service.product.IProductService;
import com.cg.service.user.IUserService;
import com.cg.util.AppUtil;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")

public class CartRestController {

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

    /*tìm id trong ở userId, ở bên phần ajax ta đã tìm ra id của khách hàng đang mua sản phẩm,*/
    /*trong phần cart(cartInfo) có chứa userId, ta tìm userId ở đó, nếu tồn tại , vượt qua trả về 1 list trong cartItem(Các trường của nó) */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCartItem(@PathVariable long id) {

        /*Tìm theo thứ tự, trong phần cartInfo, tìm thằng userDTO(id) có tồn tài không, còn thằng CartItemDTO, thì tìm xem thằng Cart(id) có tồn tại không, thành công thì trả về list CartItemDTO */
        Optional<UserDTO> userDTO = userService.findUserDTOById(id);
        if (!userDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy Người Dùng");
        }

        Long userId = userDTO.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        Map<String, String> result = new HashMap<>();

        if (!cartInfoDTO.isPresent()) {
            result.put("noCart", "Giỏ hàng của bạn đang trống");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        /*Tìm thấy Id CartInfoDTO có tồn tại, thì truyền vào cart Item để kiểm tra, nếu có thì thực hiện thành công*/
        /*CartInfoDTO là thằng cartDTO, tìm thấy id của nó tồn tại, thì gán vào, Trong CartItem có trường chứa trường cartid, nên phải lấy nó để truyền vào để so sánh khớpk không*/
        Long cartId = cartInfoDTO.get().getId();

        /*gán vào thằng CartItemDTO, nếu có tồn tại, thì ta sẽ return về tất các trường nằm trường CartItemDTO(title,url, price, totalPrice..v.v) */
        List<CartItemDTO> cartItemDTOList = cartItemService.findCartItemDTOByCartId(cartId);

        return new ResponseEntity<>(cartItemDTOList, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@Valid @RequestBody CartDTO cartDTO, BindingResult bindingResult) {

        new CartDTO().validate(cartDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<UserDTO> userDTO = userService.findUserDTOById(Long.parseLong(cartDTO.getUserId()));
        if (!userDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID Người Dùng");
        }

        Optional<ProductDTO> productDTO = productService.findProductDTOById(Long.parseLong(cartDTO.getProductId()));
        if (!productDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID Sản phẩm");
        }

        /*Tìm lấy id của userDTO ra , để so sánh có khớp với thông tin của id khách hàng trong thằng cartInfoDTO không, nó có chưa trường user*/
        Long userId = userDTO.get().getId();

        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        String quantity = cartDTO.getQuantity();

        BigDecimal price = new BigDecimal(Long.parseLong(productDTO.get().getPrice()));

        /*Hàm nhân giá với số lượng để tính ra tổng tiền */
        BigDecimal grandTotal = price.multiply(new BigDecimal(Long.parseLong(quantity)));

        /*2 thằng này đang trống, ta phải set lại truyền giá trị vào cho nó*/
        CartItem cartItem = new CartItem();

        Cart cart = new Cart();

        Map<String, Object> result = new HashMap<>();

        String success, successFirst;

        /*Nếu giỏ hàng chưa tồn tại thì ta tiến hành Tạo giỏ hàng cho nó*/
        if (!cartInfoDTO.isPresent()) {

            cart.setUser(userDTO.get().toUser());
            cart.setGrandTotal(grandTotal);/*Tổng tiền truyền vào cart*/

            cartItem = new CartItem();
            cartItem.setPrice(price);/*Hiển thị giá*/
            cartItem.setQuantity(Integer.parseInt(quantity));/*Hiển thị số lượng*/
            cartItem.setTitle(productDTO.get().getName());
            cartItem.setTotalPrice(grandTotal);/*Tổng tiền*/
            cartItem.setProduct(productDTO.get().toProduct());

            try {
                /*Vượt qua các điều kiện thì thực hiện thành công*/
                cartService.addNewCart(cart, cartItem);
                successFirst = "Tạo Mới Giỏ Hàng Thành Công";
                success = "Thêm Sản Phầm Thành Công";
                result.put("successFirst", successFirst);
                result.put("success", success);
            } catch (DataInputException e) {
                throw new DataInputException("No Error");
            }

        } else { /*Nếu tồn tại thì*/
            /*Trên database có cart_id và product_id(Ta lấy giá trị nó ra)*/
            Long cartId = cartInfoDTO.get().getId();
            Long productId = productDTO.get().getId();

            Optional<CartItemDTO> cartItemDTO = cartItemService.findCartItemDTOByCartIdAndProductId(cartId, productId);

            if (!cartItemDTO.isPresent()) {

                cartItem.setPrice(price);
                cartItem.setQuantity(Integer.parseInt(quantity));

                String name = productDTO.get().getName();/*Cách làm nếu title không nhận các thuộc tính và có giá trị null*/
                System.out.println(name);
                cartItem.setTitle(name);
                cartItem.setTotalPrice(grandTotal);/*Tổng tiền*/
                cartItem.setProduct(productDTO.get().toProduct());/*cartItem có chưa product,  setProduct*/

                cart = cartInfoDTO.get().toCart();/*cartItem có chưa cart,lấy ra để setCart*/
                cartItem.setCart(cart);
                cart.setGrandTotal(cart.getGrandTotal().add(grandTotal)); /*Tổng tiền cũng truyền vào thằng cart*/
                try {

                    cartService.addProductByCart(cart, cartItem);
                    success = "Thêm Mới Sản Phẩm Thành Công";
                    result.put("success", success);
                } catch (DataInputException e) {
                    throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
                }
                return new ResponseEntity<>(result, HttpStatus.CREATED);

            } else {

                cartItem = cartItemDTO.get().toCartItem();
                cartItem.setQuantity(cartItem.getQuantity() + Integer.parseInt(quantity));
                cartItem.setTotalPrice(cartItem.getTotalPrice().add(grandTotal));

                cart = cartInfoDTO.get().toCart();
                cart.setGrandTotal(cart.getGrandTotal().add(grandTotal));

                try {
                    cartService.updateProductByCart(cart, cartItem);
                    success = new String("Cập Nhật Sản Phẩm Thành Công");
                    result.put("success", success);
                } catch (DataInputException e) {
                    throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /*xóa theo id sản phẩm(product)(khi xóa vẫn tính lại tổng tiền còn lại của nó)*/
    /*ở ajax, ta cũng phải tìm ra uerId và producId, để thực hiện thao tác xóa đi sản phẩm*/
    @PostMapping("/remove-cart-item")
    public ResponseEntity<?> doRemoveCartItem(@Valid @RequestBody CartDTO cartDTO, BindingResult bindingResult) {

        new CartDTO().validate(cartDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<UserDTO> userDTO = userService.findUserDTOById(Long.parseLong(cartDTO.getUserId()));
        if (!userDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID Người Dùng");
        }

        Optional<ProductDTO> productDTO = productService.findProductDTOById(Long.parseLong(cartDTO.getProductId()));
        if (!productDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID sản phẩm");
        }

        /*trong cartrepository có chứa phần user, triển khai tìm các trường theo điều kiện userId có tồn tại không, ta lấy userId để truyền trực tiếp vào nó*/
        Long userId = userDTO.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        Cart cart = new Cart();
        Map<String, Object> result = new HashMap<>();
        String success;

        if (!cartInfoDTO.isPresent()) {
            throw new ResourceNotFoundException("Người Dùng Chưa Có Giỏ Hàng");
        } else {/*Nếu tồn tại thì tiếp tục xử lí*/
            /*Xử lí phần cartItem,   tìm theo cartId & productId có tồn tại không*/
            Long cartId = cartInfoDTO.get().getId();
            Long productId = productDTO.get().getId();

            Optional<CartItemDTO> cartItemDTO = cartItemService.findCartItemDTOByCartIdAndProductId(cartId, productId);
            if (!cartItemDTO.isPresent()) {
                throw new ResourceNotFoundException("Sản Phẩm Không Tồn Tại Trong Giỏ Hàng");
            } else {/*Nếu tìm thấy cartId & productId ta tiến hành xử lí dữ liệu, cập nhật lại số tiền khi xóa sản phẩm đó*/

                String totalPrice = cartItemDTO.get().getTotalPrice();/*Lấy tổng tiền ra ở cartItemDTO*/

                BigDecimal grandTotalCartInfoDTO = new BigDecimal(cartInfoDTO.get().getGrandTotal());/*Lấy tổng tiền này của Cart, để trừ đi tổng tiền của CartItemDTO */

                BigDecimal grandTotalEnd = grandTotalCartInfoDTO.subtract(new BigDecimal(totalPrice));/*Tạo 1 biến grandTotalEnd rổng để xử lí ( Lấy tổng tiền này của Cart, để trừ đi tổng tiền của CartItemDTO, ta parse totalPrice String về BigDecimal) */

                /*Hoàn thành xử lí truyền giá trị tiền vừa tính được vào lại tổng tiền ở phần cart*/
                cart = cartInfoDTO.get().toCart();
                cart.setGrandTotal(grandTotalEnd);

                Long cartItemId = cartItemDTO.get().getId();/*lấy id của cartItemDTO ra, để truyền vào xử lí*/

                try {
                    CartInfoDTO cartInfoDTONew = cartService.doRemoveCartItem(cart,  cartItemId);

                    success = "Xóa sản phẩm thành công";
                    result.put("success", success);
                    result.put("cartInfo", cartInfoDTONew);


                } catch (DataInputException e) {
                    throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED); /*Xử lí thành công thì trả về list result để hiển thị thông báo */

    }


    /*hàm tăng số lượng lên 1, (kiểm tra tất cả điều kiện), tính giá tiền nhân với số lượng, khi tăng lên 1, và lấy tổng tiền + giá tiền cộng thêm*/
    @PostMapping("/increase")
    public ResponseEntity<?> doIncreaseCart(@Valid @RequestBody CartDTO cartDTO, BindingResult bindingResult) {

        new CartDTO().validate(cartDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<UserDTO> userDTO = userService.findUserDTOById(Long.parseLong(cartDTO.getUserId()));
        if (!userDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID Người Dùng");
        }

        Optional<ProductDTO> productDTO = productService.findProductDTOById(Long.parseLong(cartDTO.getProductId()));
        if (!productDTO.isPresent()) {
            throw new ResourceNotFoundException("Không Tìm Thấy ID sản phẩm");
        }

        /*trong cartrepository có chứa phần user, triển khai tìm các trường theo điều kiện userId có tồn tại không, ta lấy userId để truyền trực tiếp vào nó*/
        Long userId = userDTO.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        String quantity = "1";
        BigDecimal price = new BigDecimal(Long.parseLong(productDTO.get().getPrice()));

        /*Tổng tiền lấy giá nhân cho số lượng, mặc định tăng số lượng lên 1*/
        BigDecimal grandTotal = price.multiply(new BigDecimal(Long.parseLong(quantity)));


        CartItem cartItem = new CartItem();
        Cart cart = new Cart();
        Map<String, Object> result = new HashMap<>();

        String success;

        if (!cartInfoDTO.isPresent()) {
            throw new ResourceNotFoundException("Người Dùng Chưa Có Giỏ Hàng để tăng số lượng");
        } else {
            Long cartId = cartInfoDTO.get().getId();
            Long productId = productDTO.get().getId();

            Optional<CartItemDTO> cartItemDTO = cartItemService.findCartItemDTOByCartIdAndProductId(cartId, productId);
            if (!cartItemDTO.isPresent()) {
                throw new ResourceNotFoundException("Sản Phẩm Không Tồn Tại Trong Giỏ Hàng");
            } else {

                cartItem = cartItemDTO.get().toCartItem();
                cartItem.setQuantity(cartItem.getQuantity() + Integer.parseInt(quantity)); /*lấy số lượng hiện tại + thêm số lượng 1, ta mặc định quantity là 1*/
                cartItem.setTotalPrice(cartItem.getTotalPrice().add(grandTotal));  /*Tổng tiền hiện tại + thêm với tổng tiền số lượng lên*/

                cart = cartInfoDTO.get().toCart();
                cartItem.setCart(cart);

                cart.setGrandTotal(cart.getGrandTotal().add(grandTotal)); /*Hiển thị tổng tiền tất cả, ta lấy tổng tiền của Cart + tổng tiền của cartItem vào*/

                try {
                    CartItem cartItemIncrease = cartService.updateProductByCart(cart,  cartItem);
                    success = "Tăng số lượng thành công!";

                    result.put("success", success);
                    result.put("cartItem", cartItemIncrease.toCartItemDTO());

                } catch (DataInputException e) {
                    throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
                }
            }
        }
        /*thành công vượt qua tất cả thì ta trả về result(trả về thông báo đã thành công)*/
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    /*hàm giảm số lượng lên 1, (kiểm tra tất cả điều kiện), tính giá tiền nhân với số lượng, khi giảm xuống 1, và lấy tổng tiền - giá tiền giảm đi*/

    @PostMapping("/reduce")
    public ResponseEntity<?> doReduceCart(@Valid @RequestBody CartDTO cartDTO, BindingResult bindingResult) {

        new CartDTO().validate(cartDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<UserDTO> userDTO = userService.findUserDTOById(Long.parseLong(cartDTO.getUserId()));
        if(!userDTO.isPresent()){
            throw new ResourceNotFoundException("Không Tìm Thấy ID Người Dùng");
        }

        Optional<ProductDTO> productDTO = productService.findProductDTOById(Long.parseLong(cartDTO.getProductId()));
        if(!productDTO.isPresent()){
            throw new ResourceNotFoundException("Không Tìm Thấy ID sản phẩm");
        }
        /*trong cartrepository có chứa phần user, triển khai tìm các trường theo điều kiện userId có tồn tại không, ta lấy userId để truyền trực tiếp vào nó*/
        Long userId = userDTO.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);


        CartItem cartItem = new CartItem();
        Cart cart = new Cart();
        Map<String, Object> result = new HashMap<>();

        String success;

        if (!cartInfoDTO.isPresent()) {
            throw new ResourceNotFoundException("Người Dùng Chưa Có Giỏ Hàng để tăng số lượng");
        } else {
            Long cartId = cartInfoDTO.get().getId();
            Long productId = productDTO.get().getId();

            Optional<CartItemDTO> cartItemDTO = cartItemService.findCartItemDTOByCartIdAndProductId(cartId, productId);

            if (!cartItemDTO.isPresent()) {
                throw new ResourceNotFoundException("Sản Phẩm Không Tồn Tại Trong Giỏ Hàng");
            }
            else {

                if (Integer.parseInt(cartItemDTO.get().getQuantity()) == 1) {
                    throw new DataInputException("Số Lượng Tối Thiểu Là 1");
                }
                else {
                    String quantity = "1";
                    BigDecimal price = new BigDecimal(Long.parseLong(productDTO.get().getPrice()));

                    /*Tổng tiền lấy giá nhân cho số lượng, mặc định tăng số lượng lên 1*/
                    BigDecimal grandTotal = price.multiply(new BigDecimal(Long.parseLong(quantity)));

                    cartItem = cartItemDTO.get().toCartItem();
                    cartItem.setQuantity(cartItem.getQuantity() - Integer.parseInt(quantity)); /*lấy số lượng hiện tại + thêm số lượng 1, ta mặc định quantity là 1*/
                    cartItem.setTotalPrice(cartItem.getTotalPrice().subtract(grandTotal));  /*Tổng tiền hiện tại + thêm với tổng tiền số lượng lên*/

                    cart = cartInfoDTO.get().toCart();

                    cart.setGrandTotal(cart.getGrandTotal().subtract(grandTotal)); /*Hiển thị tổng tiền tất cả, ta lấy tổng tiền của Cart + tổng tiền của cartItem vào*/

                    try {
                        CartItem cartItemReduce = cartService.updateProductByCart(cart, cartItem);
                        success = "Giảm số số lượng thành công!";
                        result.put("success", success);
                        result.put("cartItemReduce", cartItemReduce.toCartItemDTO());

                    } catch (DataInputException e) {
                        throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");

                    }
                }
            }
        }
        /*thành công vượt qua tất cả thì ta trả về result(trả về thông báo đã thành công)*/
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }



}