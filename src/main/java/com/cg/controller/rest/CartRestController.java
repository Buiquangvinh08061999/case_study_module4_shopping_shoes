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

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCartItem(@PathVariable long id) {

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

        Long cartId = cartInfoDTO.get().getId();

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

        /*Tìm lấy id của userDTO ra , để so sánh có khớp với thông tin của id khách hàng trong thằng cartInfoDTO không, nó có chưa trường user, tìm thằng cart theo userId, sẽ sổ tất cả các trường, bao gồm Tổng tiền hiện tại, để ta tính toán tiếp*/
        Long userId = userDTO.get().getId();

        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        String quantity = cartDTO.getQuantity();

        BigDecimal price = new BigDecimal(Long.parseLong(productDTO.get().getPrice())); /*Lấy giá tiền chính của nó, nếu có cập nhật thì nó sẽ tự động nhân số tiền hiện tại, không bị thiệt hại cho admin*/

        BigDecimal grandTotal = price.multiply(new BigDecimal(Long.parseLong(quantity)));


        CartItem cartItem = new CartItem();
        Cart cart = new Cart();

        Map<String, Object> result = new HashMap<>();
        String success, successFirst;

        /*Nếu giỏ hàng chưa tồn tại thì ta tiến hành Tạo giỏ hàng cho nó*/
        if (!cartInfoDTO.isPresent()) {
            cart.setUser(userDTO.get().toUser());
            cart.setGrandTotal(grandTotal);

            cartItem = new CartItem();
            cartItem.setPrice(price);/*Hiển thị giá*/
            cartItem.setQuantity(Integer.parseInt(quantity));/*Hiển thị số lượng*/
            cartItem.setTitle(productDTO.get().getName());
            cartItem.setTotalPrice(grandTotal);/*Tổng tiền*/

            cartItem.setUrlImage(productDTO.get().getUrlImage());

            cartItem.setProduct(productDTO.get().toProduct());

            try {
                /*Vượt qua các điều kiện thì thực hiện thành công, các giá trị của cart và cartItem đã được tạo ra*/
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

                String name = productDTO.get().getName();
                System.out.println(name);
                cartItem.setTitle(name);
                cartItem.setTotalPrice(grandTotal);/*Tổng tiền*/
                cartItem.setUrlImage(productDTO.get().getUrlImage());
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
                return new ResponseEntity<>(result,  HttpStatus.CREATED);

            } else {
                /*Tức là sản phẩm đã có trong giỏ hàng, khi ta click vào thêm giỏ hàng tại sản phẩm ấy, thì nó phải + số lượng lên 1, giá TotalPrice cộng lại với grand đã tính ở trên để cộng lại trong cartItem, còn giá price là bất biến mặc định không thay đổi,  chỉ cần set lại cái CartItem chứa sản phẩm đó là được*/
                cartItem = cartItemDTO.get().toCartItem();
                cartItem.setQuantity(cartItem.getQuantity() + Integer.parseInt(quantity));
                cartItem.setTotalPrice(cartItem.getTotalPrice().add(grandTotal));

                cart = cartInfoDTO.get().toCart();
                cart.setGrandTotal(cart.getGrandTotal().add(grandTotal)); /*lấy tổng tiền của cart + thêm tiền của sản phẩm product đã tính ra (số lượng 1 nhân với giá trong cartItem), kết quả chính xác*/

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

    @PostMapping("/remove-cart-item")
    public ResponseEntity<?> doRemoveCartItem(@Valid @RequestBody CartDTO cartDTO, BindingResult bindingResult) {
        /*hàm xóa, kiểm tra điều kiện i hệt phần api/carts/add, có userId, productId, quantity, từ phần cart(cart.userid..v.v) được gán vào các giá trị userid, productid vào*/
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

        /*Trong cartrepository có chứa phần user, triển khai tìm các trường theo điều kiện userId có tồn tại không, ta lấy userId để truyền trực tiếp vào nó*/
        /*quan trọng, nó lấy ra được 3 trường, có trường tổng tiền hiện tại , để ta xử lí nó, khi xóa nó thì trả về lại giá tiền được tính, tức là trừ số tiền đó đi*/
        Long userId = userDTO.get().getId();
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        Cart cart = new Cart();/*bỏ phần cart rỗng ở đây, để ta set lại các giá trị vừa thay đổi vào lai đây*/
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

                String totalPrice = cartItemDTO.get().getTotalPrice();/*Lấy tổng tiền ra ở cartItemDTO, đã tính price nhân với số lượng để ta tổng tiền ở đó*/

                BigDecimal grandTotalCartInfoDTO = new BigDecimal(cartInfoDTO.get().getGrandTotal());/*Lấy tổng tiền này của Cart, để trừ đi tổng tiền của CartItemDTO */

                BigDecimal grandTotalEnd = grandTotalCartInfoDTO.subtract(new BigDecimal(totalPrice));/*Tạo 1 biến grandTotalEnd rổng để xử lí ( Lấy tổng tiền này của Cart, để trừ đi tổng tiền của CartItemDTO, ta parse totalPrice String về BigDecimal, Tại totalPrice nó sẽ lấy đc số tiền sản phẩm vì ta đã tính, còn price thì chỉ lấy đc giá tiền gốc của nó thôi) */

                /*Hoàn thành xử lí truyền giá trị tiền vừa tính được vào lại tổng tiền ở phần cart*/
                cart = cartInfoDTO.get().toCart();

                cart.setGrandTotal(grandTotalEnd);/*lấy ra được số tiền đã tính ở trên, set vào lại cart.setGrandToltal lại để hiển thị ra*/

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


    /*Hàm tăng số lượng, cũng phải truyền vào userId, productId, để ta có giá trị, set lại tổng tiền ở phần cart, và các giá trị trong cartItem, price mặc đinh, thay đổi số lượng và totalPrice*/
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

        /*Tìm tất cả trường của cart theo userId truyền vào ở trên, ta lấy được tổng giá tiền grandTotal ở cart, nếu userId đó tồn tại trong cart, trong cart có trường userId*/
        Long userId = userDTO.get().getId();/*userId được tìm thấy ở phía trên, ta gán lại vào đây*/
        Optional<CartInfoDTO> cartInfoDTO = cartService.findCartInfoDTOByUserId(userId);

        String quantity = "1";
        BigDecimal price = new BigDecimal(Long.parseLong(productDTO.get().getPrice())); /*phải lấy giá trị tiền gốc ở phía product ra*/

        /*Tổng tiền lấy giá nhân cho số lượng, mặc định tăng số lượng lên 1*/
        BigDecimal grandTotal = price.multiply(new BigDecimal(Long.parseLong(quantity)));


        CartItem cartItem = new CartItem();
        Cart cart = new Cart();
        Map<String, Object> result = new HashMap<>();

        String success;

        if (!cartInfoDTO.isPresent()) {
            throw new ResourceNotFoundException("Người Dùng Chưa Có Giỏ Hàng để tăng số lượng");
        } else {
            /*Thực hiện tìm tất cả các trường theo CartItem theo cartId và productId được tìm thấy ở trên*/
            Long cartInfoId = cartInfoDTO.get().getId();
            Long productId = productDTO.get().getId();

            Optional<CartItemDTO> cartItemDTO = cartItemService.findCartItemDTOByCartIdAndProductId(cartInfoId, productId);
            if (!cartItemDTO.isPresent()) {
                throw new ResourceNotFoundException("Sản Phẩm Không Tồn Tại Trong Giỏ Hàng");
            } else {

                cartItem = cartItemDTO.get().toCartItem(); /*cartItem chưa có giá trị, ta phải gán nó cho cartItemDTO để có các trường dữ liệu. và set lại giá trị của chúng*/
                cartItem.setQuantity(cartItem.getQuantity() + Integer.parseInt(quantity)); /*lấy số lượng hiện tại + thêm số lượng 1, ta mặc định quantity là 1*/
                cartItem.setTotalPrice(cartItem.getTotalPrice().add(grandTotal));  /*Tổng tiền hiện tại + thêm với tổng tiền số lượng lên*/

                cart = cartInfoDTO.get().toCart();/*cart chưa có giá trị, ta phải gán nó cho cartInfoDTO để có các trường dữ liệu. và set lại tổng tiền hiện tại, cộng thêm số tiền vừa tăng số lượng lên*/
                cartItem.setCart(cart);

                cart.setGrandTotal(cart.getGrandTotal().add(grandTotal)); /*Hiển thị tổng tiền tất cả, ta lấy tổng tiền của Cart + tổng tiền của cartItem vào*/

                try {
                    CartItem cartItemIncrease = cartService.updateProductByCart(cart,  cartItem);
                    success = "Tăng số lượng thành công!";

                    result.put("success", success);
                    result.put("cartItemIncrease", cartItemIncrease.toCartItemDTO());

                } catch (DataInputException e) {
                    throw new DataInputException("Liên Hệ Chủ Cửa Hàng Để Được Giải Quyết");
                }
            }
        }
        /*thành công vượt qua tất cả thì ta trả về result(trả về thông báo đã thành công)*/
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    /*hàm giảm số lượng xuống 1, (kiểm tra tất cả điều kiện), tính giá tiền nhân với số lượng, khi giảm xuống 1, và lấy tổng tiền - giá tiền giảm đi*/
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
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }




}
