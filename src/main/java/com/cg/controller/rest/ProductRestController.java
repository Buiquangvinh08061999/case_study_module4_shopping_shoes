package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
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
    public ResponseEntity<?> getAllProductDTO(){

        List<ProductDTO> productDTO = productService.findAllProductDTO();

        /*Nếu rỗng bắn ra lỗi*/
        if(productDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Validated @RequestBody ProductDTO productDTO, BindingResult bindingResult){

//        new UserDTO().validate(userDTO , bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsByName = productService.existsByName(productDTO.getName());
          if(exitsByName){
              throw new EmailExistsException("Name đã tồn tại vui lòng nhập lại!");
          }

        productDTO.setId(0L);

        Optional<Category> category = categoryService.findById(productDTO.getCategory().getId());

        if(!category.isPresent()){
            throw new EmailExistsException("ID category không tồn tại!");
        }

        try{
//            Product product = productDTO.toProduct();
            Product newProduct = productService.save(productDTO.toProduct());

            return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);

        }catch (DataIntegrityViolationException e){
            throw new DataInputException("Thông tin tài khoản không hợp lệ ");
        }

    }

}
