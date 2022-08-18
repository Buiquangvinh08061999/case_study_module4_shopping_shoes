package com.cg.model;

import com.cg.model.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Accessors(chain = true)

public class Product extends BaseEntities{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Digits(integer = 12, fraction = 0)
    private BigDecimal price;

    private int quantity;

    @Column(name = "url_image")
    private String urlImage;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "describe_product")
     private String describe;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems;


    public ProductDTO toProductDTO(){
        return new ProductDTO()
                .setId(id)
                .setUrlImage(urlImage)
                .setName(name)
                .setPrice(price.toString())
                .setQuantity(String.valueOf(quantity))
                .setDescribe(describe)
                .setCreatedAt(getCreatedAt())
                .setUpdatedAt(getUpdatedAt())
                .setCategory(category.toCategoryDTO());
    }




}
