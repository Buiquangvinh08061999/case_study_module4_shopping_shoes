package com.cg.repository;

import com.cg.model.User;
import com.cg.model.dto.CountDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username); /*phương thức này nhận giá trị tên(user.getUsername) truyền vào ở hàm login(khi ta đăng nhập vào) */

    Optional<User> findByUsername(String username);


    Boolean existsByUsername(String username);
    Boolean existsByPhone(String phone);


    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username, " +
                "u.password , " +
                "u.fullName , " +
                "u.phone , " +
                "u.urlImage,  " +
                "u.createdAt, " +
                "u.updatedAt,  " +
                "u.locationRegion,  " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false"
    )
    List<UserDTO> findAllUserDTOByDeletedIsFalse();

    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username ," +
                "u.fullName ," +
                "u.phone , " +
                "u.urlImage, " +
                "loca.provinceName , " +
                "loca.districtName , " +
                "loca.wardName , " +
                "loca.address ," +
                "r.code) " +
            "FROM User AS u " +
            "JOIN LocationRegion AS loca " +
            "ON loca.id = u.locationRegion.id " +
            "JOIN Role AS r " +
            "ON r.id = u.role.id " +
            "WHERE u.deleted = false " +
            "AND CONCAT(" +
                "u.id," +
                "u.username, " +
                "u.fullName ," +
                "u.phone , " +
                "u.urlImage, " +
                "loca.provinceName , " +
                "loca.districtName , " +
                "loca.wardName, " +
                "loca.address, " +
                "r.code) LIKE ?1 "
    )
    List<UserDTO> search(String keySearch);


    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username, " +
                "u.password , " +
                "u.fullName , " +
                "u.phone , " +
                "u.urlImage, " +
                "u.createdAt, " +
                "u.updatedAt,  " +
                "u.locationRegion,  " +
                "u.role " +
            ")" +
            "FROM User AS u " +
            "WHERE u.id = ?1 "
    )
    Optional<UserDTO> findUserDTOById(long id);


    //Kiểm tra khi update
    Boolean existsByUsernameAndIdIsNot(String username, Long id);
    Boolean existsByPhoneAndIdIsNot(String phone, Long id);

    /*Viết hàm này để truyền giá trị vào menu(show_cart), lấy giá đó để lấy id, tên người đăng nhập (và sử dụng ở HomeController để so sánh tên đăng nhập có trùng không)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username, " +
                "u.fullName" +
            ") " +
            "FROM User AS u WHERE u.username = ?1 ")

    Optional<UserDTO> findUserDTOByUsername(String username);



    /*Sắp xếp id theo thứ tự tăng dần dựa theo id(Get api/products/sortASCId)*/
    /*Hàm sắp xếp thì ta lấy tất cả các trường , và sắp xếp theo một trường ta cần sắp xếp*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone, " +
                "u.urlImage, " +
                "u.createdAt" +
                ",u.updatedAt," +
                " u.locationRegion," +
                " u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.id ASC"
    )
    List<UserDTO> findAllSortASCIdUserDTO();


    /*Sắp xếp id theo thứ tự giảm dần dựa theo id(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone, " +
                "u.urlImage," +
                "u.createdAt," +
                "u.updatedAt," +
                "u.locationRegion," +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.id DESC")
    List<UserDTO> findAllSortDESCIdUserDTO();

    /*Sắp xếp username theo thứ tự tăng dần dựa theo username(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone," +
                " u.urlImage, " +
                "u.createdAt," +
                "u.updatedAt, " +
                "u.locationRegion, " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.username ASC"
    )
    List<UserDTO> findAllSortASCUserNameUserDTO();


    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone," +
                " u.urlImage, " +
                "u.createdAt," +
                "u.updatedAt, " +
                "u.locationRegion, " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.username DESC "
    )
    List<UserDTO> findAllSortDESCUserNameUserDTO();


    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone, " +
                "u.urlImage," +
                " u.createdAt," +
                "u.updatedAt, " +
                "u.locationRegion, " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.fullName ASC"
    )
    List<UserDTO> findAllSortASCFullNameUserDTO();

    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username," +
                "u.password, " +
                "u.fullName," +
                "u.phone, " +
                "u.urlImage," +
                " u.createdAt," +
                "u.updatedAt, " +
                "u.locationRegion, " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = false " +
            "ORDER BY u.fullName DESC "
    )
    List<UserDTO> findAllSortDESCFullNameUserDTO();



    @Query("SELECT NEW com.cg.model.dto.CountDTO ( " +
                "COUNT(u.id)" +
            " ) " +
            "FROM User AS u " +
            "WHERE u.deleted = false")
    CountDTO findAllCount();



    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
                "u.id, " +
                "u.username, " +
                "u.password , " +
                "u.fullName , " +
                "u.phone , " +
                "u.urlImage,  " +
                "u.createdAt, " +
                "u.updatedAt,  " +
                "u.locationRegion,  " +
                "u.role " +
            ") " +
            "FROM User AS u " +
            "WHERE u.deleted = true "
    )
    List<UserDTO> findAllUserDTOHistoryByDeletedIsTrue();















    /*Hàm search của a Minh(hàm này nó hỗ trợ mạnh khi chúng ta không cần triển khai như trên*/
//    List<User> findAllByFullNameLikeOrPhoneLike(String fullName, String phone);
}

