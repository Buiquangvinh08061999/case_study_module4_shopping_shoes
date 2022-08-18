package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.Role;
import com.cg.model.User;
import com.cg.model.dto.ProductDTO;
import com.cg.model.dto.UserDTO;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        /*Nếu rỗng bắn ra lỗi*/
        if (productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }

    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> doCreateProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult bindingResult) {

//        new UserDTO().validate(userDTO , bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsByName = productService.existsByName(productDTO.getName());
        if (exitsByName) {
            throw new EmailExistsException("Name đã tồn tại vui lòng nhập lại!");
        }

        productDTO.setId(0L);


        Optional<Category> category = categoryService.findById(productDTO.getCategory().getId());

        if (!category.isPresent()) {
            throw new EmailExistsException("ID category không tồn tại!");
        }

        try {
//            Product product = productDTO.toProduct();
            Product newProduct = productService.save(productDTO.toProduct());

            return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Server không xử lí được ");
        }

    }

    //Ham tim kiem theo id, de truyen du lieu vao hien thi Edit
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {

        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }

    @PutMapping("/update")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> doUpdateProduct(@Validated @RequestBody ProductDTO productDTO, BindingResult bindingResult) {

        //        new UserDTO().validate(userDTO , bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

//        Boolean exitsByName = productService.existsByName(productDTO.getName());
//        if (exitsByName) {
//            throw new EmailExistsException("Name đã tồn tại vui lòng nhập lại!");
//        }


        Optional<Category> category = categoryService.findById(productDTO.getCategory().getId());

        if (!category.isPresent()) {
            throw new EmailExistsException("ID category không tồn tại!");
        }


        try {

//            Product product = productDTO.toProduct();
            Product UpdateProduct = productService.save(productDTO.toProduct());

            return new ResponseEntity<>(UpdateProduct.toProductDTO(), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Thông tin tài khoản không hợp lệ ");
        }
    }


    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> getSearchProductDTO(@PathVariable String keyword) {

        List<ProductDTO> productDTO = productService.searchAllProduct(keyword);

        if (productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
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


}
