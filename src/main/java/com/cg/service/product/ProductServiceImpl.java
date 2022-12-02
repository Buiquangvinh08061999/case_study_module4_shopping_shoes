package com.cg.service.product;

import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.dto.CountDTO;
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


    /*Tìm id của product để kiểm tra có tồn tại không đưa vào phần CartRestController*/
    @Override
    public  Optional<ProductDTO> findProductDTOById(long id) {
        return productRepository.findProductDTOById(id);
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
    public List<ProductDTO> searchAllProduct(String keyword) {
        return productRepository.search(keyword);
    }


    @Override
    public List<ProductDTO> findAllSortASCIdProductDTO() {
        return productRepository.findAllSortASCIdProductDTO();
    }


    @Override
    public List<ProductDTO> findAllSortDESCIdProductDTO() {
        return productRepository.findAllSortDESCIdProductDTO();
    }

    @Override
    public List<ProductDTO> findAllSortASCNameProductDTO() {
        return productRepository.findAllSortASCNameProductDTO();
    }

    @Override
    public List<ProductDTO> findAllSortDESCNameProductDTO() {
        return productRepository.findAllSortDESCNameProductDTO();
    }

    @Override
    public List<ProductDTO> findAllSortASCPriceProductDTO() {
        return productRepository.findAllSortASCPriceProductDTO();
    }
    @Override
    public List<ProductDTO> findAllSortDESCPriceProductDTO() {
        return productRepository.findAllSortDESCPriceProductDTO();
    }
    @Override
    public Boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Boolean existsByNameAndIdIsNot(String name, Long id) {
        return productRepository.existsByNameAndIdIsNot(name, id);
    }

    @Override
    public CountDTO findAllCount() {
        return productRepository.findAllCount();
    }

    @Override
    public List<ProductDTO> findAllHistoryProductDTO() {
        return productRepository.findAllHistoryProductDTO();
    }


    @Override
    public List<ProductDTO> findAllBetWeenPriceProductDTO() {
        return productRepository.findAllBetWeenPriceProductDTO();
    }

    @Override
    public List<ProductDTO> findAllBetWeenPrice100_200ProductDTO() {
        return productRepository.findAllBetWeenPrice100_200ProductDTO();
    }

    @Override
    public List<ProductDTO> findAllBetWeenPrice200_300ProductDTO() {
        return productRepository.findAllBetWeenPrice200_300ProductDTO();
    }

    @Override
    public List<ProductDTO> findAllBetWeenPrice300_500ProductDTO() {
        return productRepository.findAllBetWeenPrice300_500ProductDTO();
    }
}
