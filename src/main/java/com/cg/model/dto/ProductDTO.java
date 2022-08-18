package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class ProductDTO {

    private Long id;

    @NotBlank(message = "Ảnh không được để trống")
    private String urlImage;

    @NotBlank(message = "Tên là bắt buộc")
//    @Pattern(regexp = "^[a-zA-Z0-9ứáàớ\\s]*$", message = "regex chuẩn có dấu")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Tên không có kí tự đặt biệt")
    private String name;

    @NotBlank(message = "Price là bắt buộc")
    @Pattern(regexp = "^\\d+$", message = "Price không được nhập số âm hoặc chữ, yêu cầu nhập lại")
    @Min(value = 50000, message = "Giá tối thiểu là 50000")
    @Max(value = 10000000, message = "Giá lớn nhất là 10000000")
    private String price;


    @Pattern(regexp = "^[0-9]+$", message = "Quantity only digit")
    private String quantity = "1";


    @NotBlank(message = "Mô tả là bắt buộc")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Mô tả phải là chữ , không có kí tự đặt biệt và số")
    private String describe;


    private boolean deleted;

    private CategoryDTO category;

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
}
