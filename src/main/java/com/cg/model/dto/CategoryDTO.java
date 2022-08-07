package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class CategoryDTO {

    @NotNull(message = "The role is required")
    private Long id;

    private String title;

    public Category toCategory() {
        return new Category()
                .setId(id)
                .setTitle(title);
    }

}
