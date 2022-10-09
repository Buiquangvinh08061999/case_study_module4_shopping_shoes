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

    /*Hàm có sẳn*/
    Boolean existsByPhone(String phone);



    /*Hiển thị tất cả thông tin User ra với điều kiện nó còn tộn tại(nếu p.deleted = false, = true thì nó đã xóa)*/
    /*Phần này viết ở /api/customers, (GET), chỉ cần vào ajax viết hàm, dùng foreach để duyệt hết và gọi lại api như trên, sẽ có tất cả thông tin, ta chỉ truyền vào, và vẽ lại phần body*/
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
            "u.role ) " +
            "FROM User AS u " +
            "WHERE u.deleted = false")
    List<UserDTO> findAllUserDTOByDeletedIsFalse();

    /*Phần này viết ở /api/customers/search (POST) */
    /*Search tất cả thông tin ra, JOIN 3 bản lại với nhau để tìm kiếm (tỉnh, huyện, thị trấn, địa chỉ) phía bảng khoái ngoại LocationRegion(làm vậy với role), nhưng các thuộc tính các trường đó ta tạo ở UserDTO luôn,(để có thể search ra các trường của các bản JOIN lại)*/
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
                "r.code) LIKE ?1 ")

    List<UserDTO> search(String keySearch);


    /*Hien thi danh sach dua theo userid, trong phần cart có user_id, Ta khởi tạo để kiểm tra có tồn tại userId hay không*/
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
            "u.role ) " +
            "FROM User AS u " +
            "WHERE u.id = ?1 ")
    Optional<UserDTO> findUserDTOById(long id);


    /*Cập nhật cho trùng tên của nó, nhưng không trùng tên ở các mục khác*/
    Boolean existsByUsernameAndIdIsNot(String username, Long id);

    Boolean existsByPhoneAndIdIsNot(String phone, Long id);

    /*Viết hàm này để truyền giá trị vào menu(show_cart), lấy giá đó để lấy id, tên người đăng nhập (và sử dụng ở HomeController để so sánh tên đăng nhập có trùng không)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(" +
            "u.id, " +
            "u.username, " +
            "u.fullName) " +
            "FROM User AS u WHERE u.username =?1 ")

    Optional<UserDTO> findUserDTOByUsername(String username);



    /*Sắp xếp id theo thứ tự tăng dần dựa theo id(Get api/products/sortASCId)*/
    /*Hàm sắp xếp thì ta lấy tất cả các trường , và sắp xếp theo một trường ta cần sắp xếp*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.id ASC")
    List<UserDTO> findAllSortASCIdUserDTO();


    /*Sắp xếp id theo thứ tự giảm dần dựa theo id(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.id DESC")
    List<UserDTO> findAllSortDESCIdUserDTO();

    /*Sắp xếp username theo thứ tự tăng dần dựa theo username(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.username ASC ")
    List<UserDTO> findAllSortASCUserNameUserDTO();


    /*Sắp xếp username theo thứ tự giảm dần dựa theo username(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.username DESC")
    List<UserDTO> findAllSortDESCUserNameUserDTO();

    /*Sắp xếp fullName theo thứ tự tăng dần dựa theo fullName(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.fullName ASC")
    List<UserDTO> findAllSortASCFullNameUserDTO();

    /*Sắp xếp fullName theo thứ tự giảm dần dựa theo fullName(Get api/products/sortASCId)*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username,u.password, u.fullName,u.phone, u.urlImage, u.createdAt,u.updatedAt, u.locationRegion, u.role ) FROM User AS u WHERE u.deleted = false ORDER BY u.fullName DESC")
    List<UserDTO> findAllSortDESCFullNameUserDTO();


    /*Đếm tổng số lượng danh sách người dùng, đang tốn tại(Get api/customers/count)*/
    @Query("SELECT NEW com.cg.model.dto.CountDTO ( COUNT(u.id) ) FROM User AS u WHERE u.deleted = false")
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
            "u.role ) " +
            "FROM User AS u " +
            "WHERE u.deleted = true ")
    List<UserDTO> findAllUserDTOHistoryByDeletedIsTrue();















    /*Hàm search của a Minh(hàm này nó hỗ trợ mạnh khi chúng ta không cần triển khai như trên*/
//    List<User> findAllByFullNameLikeOrPhoneLike(String fullName, String phone);
}

