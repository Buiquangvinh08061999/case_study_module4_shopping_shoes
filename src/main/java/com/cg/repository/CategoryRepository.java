package com.cg.repository;

import com.cg.model.Category;
import com.cg.model.Role;
import com.cg.model.dto.CategoryDTO;
import com.cg.model.dto.RoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query("SELECT NEW com.cg.model.dto.CategoryDTO(c.id, c.title) FROM Category AS c")
    List<CategoryDTO> findAllCategoryDTO();


    /*Kiểm tra tên đã tồn tại chưa*/
    Boolean existsByTitle(String title);
}
