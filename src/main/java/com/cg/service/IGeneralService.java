package com.cg.service;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {

    List<T> findAll();

    Optional<T> findById(Long id); /*tìm tất cả thông tin theo id truyền vào(productId lấy id từ finAllProduct .. tương tự với user cũng vậy), dùng để xóa mềm và khôi phục lại theo Id*/

    T getById(Long id);

    T save(T t);

    void remove(Long id);
}
