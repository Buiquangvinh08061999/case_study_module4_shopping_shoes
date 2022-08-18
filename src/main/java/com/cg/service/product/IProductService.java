package com.cg.service.product;

import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.service.IGeneralService;

import java.util.List;

public interface IProductService extends IGeneralService<Product> {

    List<ProductDTO> findAllProductDTO();

    Boolean existsByName(String name);

//    Boolean existByUrlImage(String urlImage);

    List<ProductDTO> searchAllProduct(String keywork);
}
