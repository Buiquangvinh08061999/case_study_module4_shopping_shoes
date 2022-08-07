package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class ProductDTO {

    private Long id;

    private String urlImage;
    private String name;
    private String price;
    private String quantity;
    private String describe;

    private boolean deleted;

    private CategoryDTO category;

    public ProductDTO(Long id, String urlImage, String name, BigDecimal price, Integer quantity, String describe, Category category){
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.price = price.toString();
        this.quantity = quantity.toString();
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
