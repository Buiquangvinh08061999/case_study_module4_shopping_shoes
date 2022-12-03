package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductDTO implements Validator {

    private Long id;

    private String urlImage;

    private String name;

    private String price;


    private String quantity;


    private String describe;

    private boolean deleted;

    private CategoryDTO category;

    /*trường title ở bên danh mục(category) nhưng ta phải tạo thêm ở product, để có thể search ra đúng tên của nó*/
    private String title;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public ProductDTO(Long id, String urlImage, String name, BigDecimal price, Integer quantity, String describe, Date createdAt,  Date updatedAt , Category category){
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price.toString();
        this.quantity = quantity.toString();
        this.describe = describe;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category.toCategoryDTO();
    }



    /*Hàm search víp cách cũ của a Minh, tức là còn join bảng với nhau, và phảo tạo thêm trường title của catelory, qua product, tốn bộ nhớ hơn*/
    public ProductDTO(Long id, String urlImage, String name, BigDecimal price, String describe, String title) {
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price.toString();
        this.describe = describe;
        this.title = title;
    }

    /*hàm search không cần join bảng, tại vì 2 thằng này đã có quan hệ với nhau*/
    public ProductDTO(Long id, String urlImage, String name, BigDecimal price, String describe, Category category){
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price.toString();
        this.describe = describe;
        this.category = category.toCategoryDTO();
    }

    public Product toProduct() {
        return new Product()
                .setId(id)
                .setUrlImage(urlImage)
                .setName(name)
                .setPrice(new BigDecimal(Long.parseLong(price)))
                .setQuantity(Integer.parseInt(quantity))
                .setDescribe(describe)
                .setCategory(category.toCategory());

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDTO productDTO = (ProductDTO) target;

        String name = productDTO.getName();
        String urlImage = productDTO.getUrlImage();
        String price = productDTO.getPrice();
        String describe = productDTO.getDescribe();

        if(name.trim().isEmpty() && urlImage.trim().isEmpty() && price.trim().isEmpty() && describe.trim().isEmpty()){
            errors.rejectValue("name",  "name.isEmpty" ,"Vui lòng nhập vào tên sản phẩm, tên sản phẩm không được để trống");
            errors.rejectValue("urlImage",  "urlImage.isEmpty" ,"Vui lòng nhập vào địa chỉ ảnh, địa chỉ ảnh không được để trống");
            errors.rejectValue("price",  "price.isEmpty" ,"Vui lòng nhập giá tiền, giá tiền không được để trống");
            errors.rejectValue("describe",  "describe.isEmpty" ,"Vui lòng nhập vào mô tả, mô tả không được để trống");
            return;
        }

        if(name.trim().isEmpty()){
            errors.rejectValue("name",  "name.isEmpty" ,"Vui lòng nhập vào tên sản phẩm, tên sản phẩm không được để trống");
            return;
        }
        if(name.trim().replaceAll("\\s+", "").length() < 5 || name.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("name",  "name.length" ,"Tên phải nằm trong khoảng từ 5 Đến 255 Ký Tự");
            return;
        }
        if(!name.matches("^([a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+)$")){
            errors.rejectValue("name",  "name.matches" ,"Tên sản phẩm không chứa kí tự đặt biệt, Vui lòng nhập lại tên đúng quy định!");
            return;
        }


        if(urlImage.trim().isEmpty()){
            errors.rejectValue("urlImage",  "urlImage.isEmpty" ,"Vui lòng nhập vào địa chỉ ảnh, địa chỉ ảnh không được để trống");
            return;
        }


        if(price.trim().isEmpty()){
            errors.rejectValue("price",  "price.isEmpty" ,"Vui lòng nhập giá tiền, giá tiền không được để trống");
            return;
        }
        if(!price.matches("^\\d+$")){
            errors.rejectValue("price",  "price.matches" ,"Price không được nhập chữ hoặc số âm, kí tự đặt biệt, Yêu cầu nhập đúng định dạng số vào!");
            return;
        }
        BigDecimal newPrice = new BigDecimal(Long.parseLong(price));
        BigDecimal min = new BigDecimal(50000L);
        BigDecimal max = new BigDecimal(1000000L);
        if(newPrice.compareTo(min) < 0){
            errors.rejectValue("price",  "price.min" ,"Price số tiền tối thiểu là 50.000 đ");
            return;
        }
        if(newPrice.compareTo(max) > 0){
            errors.rejectValue("price",  "price.max" ,"Price số tiền tối đa 1.000.000 đ");
            return;
        }

        if(describe.trim().isEmpty()){
            errors.rejectValue("describe",  "describe.isEmpty" ,"Vui lòng nhập vào mô tả, mô tả không được để trống");
            return;
        }
        if(describe.trim().replaceAll("\\s+", "").length() < 5 || describe.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("describe",  "describe.length" ,"Mô tả phải nằm trong khoảng từ 5 Đến 255 Ký Tự");
            return;
        }


    }
}
