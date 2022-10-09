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

    /*Phần này ở api/product/{id} (GET) (ta truyền id vào, tìm id (productId) ở bên phần ajax */
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

    /*Hàm tìm kiếm tất cả các trường theo id tăng dần sortASC theo id*/
    @Override
    public List<ProductDTO> findAllSortASCIdProductDTO() {
        return productRepository.findAllSortASCIdProductDTO();
    }

    /*Hàm tìm kiếm tất cả các trường theo id giảm dần sortDESC theo id*/
    @Override
    public List<ProductDTO> findAllSortDESCIdProductDTO() {
        return productRepository.findAllSortDESCIdProductDTO();
    }

    /*Hàm tìm kiếm tất cả các trường theo tên tăng dần sortASC theo name*/
    @Override
    public List<ProductDTO> findAllSortASCNameProductDTO() {
        return productRepository.findAllSortASCNameProductDTO();
    }

    /*Hàm tìm kiếm tất cả các trường theo tên giảm dần sortDESC theo name*/
    @Override
    public List<ProductDTO> findAllSortDESCNameProductDTO() {
        return productRepository.findAllSortDESCNameProductDTO();
    }

    /*Hàm tìm kiếm tất cả các trường theo Giá tăng dần sortASC theo price*/
    @Override
    public List<ProductDTO> findAllSortASCPriceProductDTO() {
        return productRepository.findAllSortASCPriceProductDTO();
    }
    /*Hàm tìm kiếm tất cả các trường theo Giá giảm dần sortDESC theo price*/
    @Override
    public List<ProductDTO> findAllSortDESCPriceProductDTO() {
        return productRepository.findAllSortDESCPriceProductDTO();
    }
    /*dùng kiểm tra ở create*/
    @Override
    public Boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    /*dùng kiểm tra ở update*/
    @Override
    public Boolean existsByNameAndIdIsNot(String name, Long id) {
        return productRepository.existsByNameAndIdIsNot(name, id);
    }

    /*đếm số lượng tổng sản phẩm*/
    @Override
    public CountDTO findAllCount() {
        return productRepository.findAllCount();
    }

    /*hiển thị lịch sử sản phẩm đã xóa*/
    @Override
    public List<ProductDTO> findAllHistoryProductDTO() {
        return productRepository.findAllHistoryProductDTO();
    }


    /*Lấy ra số tiền(price) trong khoảng 50k đến 100k với điều kiện deleted =0, tức là còn tồn tại*/
    @Override
    public List<ProductDTO> findAllBetWeenPriceProductDTO() {
        return productRepository.findAllBetWeenPriceProductDTO();
    }

    /*Lấy ra số tiền(price) trong khoảng 100k đến 200k với điều kiện deleted =0, tức là còn tồn tại*/
    @Override
    public List<ProductDTO> findAllBetWeenPrice100_200ProductDTO() {
        return productRepository.findAllBetWeenPrice100_200ProductDTO();
    }

    /*Lấy ra số tiền(price) trong khoảng 200k đến 300k với điều kiện deleted =0, tức là còn tồn tại*/
    @Override
    public List<ProductDTO> findAllBetWeenPrice200_300ProductDTO() {
        return productRepository.findAllBetWeenPrice200_300ProductDTO();
    }

    /*Lấy ra số tiền(price) trong khoảng 300k đến 500k với điều kiện deleted =0, tức là còn tồn tại*/
    @Override
    public List<ProductDTO> findAllBetWeenPrice300_500ProductDTO() {
        return productRepository.findAllBetWeenPrice300_500ProductDTO();
    }
}
