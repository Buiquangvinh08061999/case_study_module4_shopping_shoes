package com.cg.service.product;

import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.repository.CategoryRepository;
import com.cg.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> findAllProductDTO() {
        return productRepository.findAllProductDTO();
    }


    @Override

    public List<Product> findAll() {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product getById(Long id) {
        return null;
    }

    @Override
    public Product save(Product product) {
        Category category = categoryRepository.save(product.getCategory());

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public List<ProductDTO> searchAllProduct(String keywork) {
        return productRepository.search(keywork);
    }


}
