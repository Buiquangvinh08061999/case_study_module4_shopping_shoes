package com.cg.model;


import com.cg.model.dto.CategoryDTO;
import com.cg.model.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
@Accessors(chain = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(targetEntity = Product.class, mappedBy = "category" , fetch = FetchType.EAGER)
    private Set<Product> products;


    public CategoryDTO toCategoryDTO(){
        return new CategoryDTO()
                .setId(id)
                .setTitle(title);

    }

}
