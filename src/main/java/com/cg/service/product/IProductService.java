package com.cg.service.product;

import com.cg.model.Product;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product> {
    /*Hiển thị tất cả danh sách */
    List<ProductDTO> findAllProductDTO();


    /*Hàm search*/
    List<ProductDTO> searchAllProduct(String keyword);

    /*Lấy tất cả thông tin các trường ra theo id truyền vào*/
    Optional<ProductDTO> findProductDTOById(long id);


    /*Hàm tìm kiếm tất cả các trường theo id tăng dần sortASCId theo id*/
    List<ProductDTO> findAllSortASCIdProductDTO();

    /*Hàm tìm kiếm tất cả các trường theo id giảm dần sortASCId theo id*/
    List<ProductDTO> findAllSortDESCIdProductDTO();

    /*Hàm tìm kiếm tất cả các trường theo tên tăng dần sortASCName theo name*/
    List<ProductDTO> findAllSortASCNameProductDTO();

    /*Hàm tìm kiếm tất cả các trường theo tên giảm dần sortASCName theo name*/
    List<ProductDTO> findAllSortDESCNameProductDTO();


    /*Hàm tìm kiếm tất cả các trường theo giá tăng dần sortASCPrice theo price*/
    List<ProductDTO> findAllSortASCPriceProductDTO();

    /*Hàm tìm kiếm tất cả các trường theo giá giảm dần sortDESCPrice theo price*/
    List<ProductDTO> findAllSortDESCPriceProductDTO();

    Boolean existsByName(String name);/*Kiểm tra ở create,khi thêm mới */
    Boolean existsByNameAndIdIsNot(String name, Long id); /*Kiểm tra ở update,khi cập nhật */


    /*Đếm  tổng số lượng sản phẩm có trong product */
    CountDTO findAllCount();

    /*Hiển thị tất cả danh sách history, deleted =1, tức là đã xóa ,  = true */
    List<ProductDTO> findAllHistoryProductDTO();



    /*Lấy ra số tiền(price) trong khoảng 50k đến 100k với điều kiện deleted =0, tức là còn tồn tại*/
    List<ProductDTO> findAllBetWeenPriceProductDTO();

    /*Lấy ra số tiền(price) trong khoảng 100k đến 200k với điều kiện deleted =0, tức là còn tồn tại*/
    List<ProductDTO> findAllBetWeenPrice100_200ProductDTO();

    /*Lấy ra số tiền(price) trong khoảng 200k đến 300k với điều kiện deleted =0, tức là còn tồn tại*/
    List<ProductDTO> findAllBetWeenPrice200_300ProductDTO();

    /*Lấy ra số tiền(price) trong khoảng 300k đến 500k với điều kiện deleted =0, tức là còn tồn tại*/
    List<ProductDTO> findAllBetWeenPrice300_500ProductDTO();

}
