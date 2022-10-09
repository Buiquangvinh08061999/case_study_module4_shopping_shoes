package com.cg.service.category;

import com.cg.model.Category;
import com.cg.model.Role;
import com.cg.model.dto.CategoryDTO;
import com.cg.service.IGeneralService;

import java.util.List;

public interface ICategoryService extends IGeneralService<Category> {

    /*Phương thức mẫu để hiểu hàm save ở IGeneralService */
    Category save(Category category);


    List<CategoryDTO> findAllCategoryDTO();


    Boolean existsById(Long id);

    Boolean existsByTitle(String title);
}
