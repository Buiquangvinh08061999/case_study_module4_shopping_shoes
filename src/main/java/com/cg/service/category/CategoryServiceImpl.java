package com.cg.service.category;

import com.cg.model.Category;
import com.cg.model.Role;
import com.cg.model.dto.CategoryDTO;
import com.cg.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category getById(Long id) {
        return null;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }


    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }


    @Override
    public List<CategoryDTO> findAllCategoryDTO() {
        return categoryRepository.findAllCategoryDTO();
    }


    @Override
    public Boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }


    @Override
    public Boolean existsByTitle(String title) {
        return categoryRepository.existsByTitle(title);
    }


}
