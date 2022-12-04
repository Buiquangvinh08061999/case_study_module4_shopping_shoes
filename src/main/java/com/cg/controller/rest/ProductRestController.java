package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.Role;
import com.cg.model.User;
import com.cg.model.dto.*;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private AppUtil appUtils;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;


    @GetMapping
    public ResponseEntity<?> getAllProductDTO() {

        List<ProductDTO> productDTO = productService.findAllProductDTO();

        if (productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }


    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    /*@RequestBody gửi lên, còn @ResponseBody là đẩy giá trị string lên */
    public ResponseEntity<?> doCreateProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult bindingResult) {

        new ProductDTO().validate(productDTO , bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsByName = productService.existsByName(productDTO.getName());
        if (exitsByName) {
            throw new EmailExistsException("Tên " +productDTO.getName()+ " bạn vừa nhập vào đã tồn tại.Vui lòng nhập lại!");
        }

        productDTO.setId(0L);
        productDTO.setQuantity(String.valueOf(1)); /*set quantity mặc định là 1*/

        Optional<Category> category = categoryService.findById(productDTO.getCategory().getId());

        if (!category.isPresent()) {
            throw new EmailExistsException("ID category không tồn tại!");
        }

        try {
            Product newProduct = productService.save(productDTO.toProduct());

            return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);

        } catch (DataInputException e) {
            throw new DataInputException("Server không xử lí được ");
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable long id) {

        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy Id Product");
        }

        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }


    @PutMapping("/update")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> doUpdateProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult bindingResult) {

        new ProductDTO().validate(productDTO , bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        /*Cho trùng tên hiện tại, Nhưng không được trùng tên của sản phẩm khác, Chỉ lấy được tên của giá trị khác, không lấy được id giá trị khác, ID này là ID của thằng ta kích vào đấy*/
        Boolean exitsByName = productService.existsByNameAndIdIsNot(productDTO.getName(), productDTO.getId());
        if(exitsByName){
            throw new EmailExistsException("Tên " + productDTO.getName() +" đã tồn tại.Vui lòng kiểm tra lại thông tin!"); /*Vì đã có giá trị nhập vào, nên ta sẽ đẩy dữ liệu ra cho chính xác*/
        }


        Optional<Category> category = categoryService.findById(productDTO.getCategory().getId());
        if (!category.isPresent()) {
            throw new EmailExistsException("ID category không tồn tại!");
        }

        try {

            Product UpdateProduct = productService.save(productDTO.toProduct());

            return new ResponseEntity<>(UpdateProduct.toProductDTO(), HttpStatus.CREATED);

        } catch (DataInputException e) {
            throw new DataInputException("Thông tin tài khoản không hợp lệ ");
        }
    }


    @PostMapping("/search")
    public ResponseEntity<?> getSearchProductDTO(@RequestBody SearchDTO searchDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        String keySearch = searchDTO.getKeySearch();
        keySearch = "%" + keySearch + "%";
        System.out.println(keySearch);

        List<ProductDTO> productDTOList = productService.searchAllProduct(keySearch);

        if (productDTOList.isEmpty()) {
            throw new DataInputException("Không tìm thấy từ khóa(  " + searchDTO.getKeySearch() + "   )vui lòng nhập lại!"); /*nếu từ khóa tìm kiếm rỗng sẽ bắn ra lỗi*/
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    //Hàm xóa mềm theo id
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> removeAllProductDTO(@PathVariable Long id) {

        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {

            product.get().setDeleted(true);

            productService.save(product.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } else {
            throw new DataInputException("Thông tin không hợp lệ");
        }

    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategoryDTO() {

        List<CategoryDTO> categoryDTO = categoryService.findAllCategoryDTO();

        if (categoryDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PostMapping("/category/create")
    public ResponseEntity<?> doCreateRoleDTO(@Validated @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult){

        new CategoryDTO().validate(categoryDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        categoryDTO.setId(0L);

        Boolean existsById = categoryService.existsById(categoryDTO.getId());
        if(existsById){
            throw new EmailExistsException("Id Category đã tồn tại");
        }

        Boolean existsByTitle = categoryService.existsByTitle(categoryDTO.getTitle());
        if(existsByTitle){
            throw new EmailExistsException("Tên danh mục của Category đã tồn tại");
        }

        try {
            Category categorySave =  categoryService.save(categoryDTO.toCategory());

            return new ResponseEntity<> (categorySave.toCategoryDTO(), HttpStatus.CREATED);
        }catch (DataInputException e){
            throw new DataInputException("Thông tin category không hợp lệ");
        }
    }

    /*Sort sắp xếp thứ tự theo các trường quan trọng*/
    /*Sắp xếp theo tên từ A->Z*/
    @GetMapping("/sortASCName")
    public ResponseEntity<?> getAllSortASCNameProductDTO() {

        List<ProductDTO> productDTO = productService.findAllSortASCNameProductDTO();


        if (productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/sortDESCName")
    public ResponseEntity<?> getAllSortDESCNameProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllSortDESCNameProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    /*Sắp xếp theo id tăng dần từ 1->99*/
    @GetMapping("/sortASCId")
    public ResponseEntity<?> getAllSortASCIdProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllSortASCIdProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    /*giảm dần theo id 99->1*/
    @GetMapping("/sortDESCId")
    public ResponseEntity<?> getAllSortDESCIdProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllSortDESCIdProductDTO();
        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    /*Sắp xếp theo price từ 1->99*/
    @GetMapping("/sortASCPrice")
    public ResponseEntity<?> getAllSortASCPriceProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllSortASCPriceProductDTO();
        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    @GetMapping("/sortDESCPrice")
    public ResponseEntity<?> getAllSortDESCPriceProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllSortDESCPriceProductDTO();
        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    /*Đếm số lượng sản phẩm*/
    @GetMapping("/count")
    public ResponseEntity<?> getAllCountProductDTO() {
        CountDTO  countDTO = productService.findAllCount();

        return new ResponseEntity<>(countDTO, HttpStatus.OK);

    }

    /*Trả về tập danh sách đã bị xóa mềm*/
    @GetMapping("/historyProduct")
    public ResponseEntity<?> getAllHistoryProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllHistoryProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);

    }

    //Hàm khôi phục theo id, set lại về false( set deleted = 0)
    @PostMapping("/restore/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> restoreAllProductDTO(@PathVariable Long id) {

        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {

            product.get().setDeleted(false);

            productService.save(product.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } else {
            throw new DataInputException("Thông tin không hợp lệ");
        }
    }

    /*Phần này được trang người dùng sử dụng(listOrder)*/
    @GetMapping("/betweenPrice50000and100000")
    public ResponseEntity<?> getAllBetWeenPriceProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllBetWeenPriceProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/betweenPrice100000and200000")
    public ResponseEntity<?> getAllBetWeenPrice100_200ProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllBetWeenPrice100_200ProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    @GetMapping("/betweenPrice200000and300000")
    public ResponseEntity<?> getAllBetWeenPrice200_300ProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllBetWeenPrice200_300ProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/betweenPrice300000and500000")
    public ResponseEntity<?> getAllBetWeenPrice300_500ProductDTO() {

        List<ProductDTO> productDTOList = productService.findAllBetWeenPrice300_500ProductDTO();

        if (productDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }






}
