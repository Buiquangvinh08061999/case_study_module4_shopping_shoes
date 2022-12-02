package com.cg.service.product;

import com.cg.model.Product;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product> {

    List<ProductDTO> findAllProductDTO();

    List<ProductDTO> searchAllProduct(String keyword);

    Optional<ProductDTO> findProductDTOById(long id);

    List<ProductDTO> findAllSortASCIdProductDTO();
    List<ProductDTO> findAllSortDESCIdProductDTO();

    List<ProductDTO> findAllSortASCNameProductDTO();
    List<ProductDTO> findAllSortDESCNameProductDTO();



    List<ProductDTO> findAllSortASCPriceProductDTO();
    List<ProductDTO> findAllSortDESCPriceProductDTO();

    Boolean existsByName(String name);
    Boolean existsByNameAndIdIsNot(String name, Long id); /*Kiểm tra ở update,khi cập nhật */



    CountDTO findAllCount();

    /*Hiển thị tất cả danh sách history, deleted =1, tức là đã xóa ,  = true */
    List<ProductDTO> findAllHistoryProductDTO();



    List<ProductDTO> findAllBetWeenPriceProductDTO();
    List<ProductDTO> findAllBetWeenPrice100_200ProductDTO();
    List<ProductDTO> findAllBetWeenPrice200_300ProductDTO();
    List<ProductDTO> findAllBetWeenPrice300_500ProductDTO();

}
