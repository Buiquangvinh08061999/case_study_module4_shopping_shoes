package com.cg.service.user;

import com.cg.model.User;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.UserDTO;
import com.cg.service.IGeneralService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {

    /*Hiển thị tất cả các trường có deleted = 0, tất là flase*/
    List<UserDTO> findAllUserDTOByDeletedIsFalse();

    /*Search tất cả các trường */
    List<UserDTO> searchAllUser(String keyword);


    // Hàm search A.Minh
//    List<User> searchAllTemp(String keyword);


    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    /*Dùng để truyền vào menu(show_cart)*/
    Optional<UserDTO> findUserDTOByUsername(String username);


    Optional<UserDTO> findUserDTOById(long id);


//    Optional<UserDTO> findUserDTOByUsername(String username);


    /*Update kiểm tra tìm theo ID*/
    Boolean existsById(Long id);

    //Kiểm tra dữ liệu đưa vào dùng cho create;
    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);

    /*Dùng cho saveupdate*/
    User saveUpdate(User user);

    //Dùng cho update, cho update lại tên cũ và sdt cũ, nhưng ko cho lặp lại ở các ID khác;
    Boolean existsByUsernameAndIdIsNot(String username, Long id);
    Boolean existsByPhoneAndIdIsNot(String phone, Long id);


    /*Hàm tìm kiếm tất cả các trường theo id tăng dần sortASCId theo id*/
    List<UserDTO> findAllSortASCIdUserDTO();

    /*Hàm tìm kiếm tất cả các trường theo id giảm dần sortDESCId theo giảm dần*/
    List<UserDTO> findAllSortDESCIdUserDTO();

    /*Hàm tìm kiếm tất cả các trường theo username tăng dần sortASCUserName theo username*/
    List<UserDTO> findAllSortASCUserNameUserDTO();

    /*Hàm tìm kiếm tất cả các trường theo username giảm dần sortDESCUserName theo username*/
    List<UserDTO> findAllSortDESCUserNameUserDTO();

    /*Hàm tìm kiếm tất cả các trường theo fullName tăng dần sortASCFullName theo fullName*/
    List<UserDTO> findAllSortASCFullNameUserDTO();

    /*Hàm tìm kiếm tất cả các trường theo fullName giảm dần sortDESCFullName theo fullName*/
    List<UserDTO> findAllSortDESCFullNameUserDTO();

    /*Đếm tổng số lượng danh sách người dùng, đang tốn tại(Get api/customers/count)*/
    CountDTO findAllCount();

    /*Hiển thị lại những danh sách đã bị xóa mềm deleted = 1, tức là true */
    List<UserDTO> findAllUserDTOHistoryByDeletedIsTrue();


}
