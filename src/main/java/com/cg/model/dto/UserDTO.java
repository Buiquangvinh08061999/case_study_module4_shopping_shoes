package com.cg.model.dto;

import com.cg.model.LocationRegion;
import com.cg.model.Role;
import com.cg.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDTO implements Validator {

    private Long id;

    private String username;

    private String password;

//    @NotBlank(message = "FullName là bắt buộc")
//    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "FullName phải là chữ , không có kí tự đặt biệt và số")
    private String fullName;

    private String phone;

    private String urlImage;

    private boolean deleted;

    @Valid
    private LocationRegionDTO locationRegion;

    @Valid
    private RoleDTO role;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;



    /*viết thêm các trường để search ra các trường này*/
    private String provinceName;
    private String districtName;
    private String wardName;
    private String address;

    private String code;



    /*Hiển thị tất cả*/
    public UserDTO(Long id, String username, String password, String fullName, String phone, String urlImage, Date createdAt, Date updatedAt, LocationRegion locationRegion, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.urlImage = urlImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.role = role.toRoleDTO();
    }

    //Search theo trường bỏ ẩn password
    public UserDTO(Long id, String username, String fullName, String phone, String urlImage, LocationRegion locationRegion, Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.urlImage = urlImage;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.role = role.toRoleDTO();
    }

    /*Hàm Search (theo cách của anh Minh)*/
    public UserDTO(Long id, String username, String fullName, String phone, String urlImage , String provinceName, String districtName, String wardName, String address, String code) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.urlImage = urlImage;
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.wardName = wardName;
        this.address = address;
        this.code = code;
    }


    /*Lấy 3 trường đề hiển thị dữ liệu ở phần menu(show_cart)*/
    public UserDTO(Long id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }



    public User toUser() {
        return (User) new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setFullName(fullName)
                .setUrlImage(urlImage)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion())
                .setRole(role.toRole())
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        String username = userDTO.getUsername();
        String fullName = userDTO.getFullName();
        String password = userDTO.getPassword();
        String phone = userDTO.getPhone();
        String address = userDTO.locationRegion.getAddress(); /*cách này dùng để viết ở đây luôn, thay vì phải qua thằng locationDTO viết*/


        if(username.trim().isEmpty() && fullName.trim().isEmpty() && password.trim().isEmpty() && phone.trim().isEmpty() && address.trim().isEmpty()){
            errors.rejectValue("username",  "username.isEmpty" ,"Vui lòng nhập vào username, username không được để trống");
            errors.rejectValue("fullName",  "fullName.isEmpty" ,"Vui lòng nhập vào fullName, fullName không được để trống");
            errors.rejectValue("password",  "password.isEmpty" ,"Vui lòng nhập vào password, password không được để trống");
            errors.rejectValue("phone",  "phone.isEmpty" ,"Vui lòng nhập vào phone, phone không được để trống");
            errors.rejectValue("address",  "address.isEmpty" ,"Vui lòng nhập vào address, address không được để trống");
            return;
        }
        /*Tổng hợp các phương thức xử lí validator username*/
        if(username.trim().isEmpty()){
            errors.rejectValue("username",  "username.isEmpty" ,"Vui lòng nhập vào username, username không được để trống");
            return;
        }
        if(!username.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$")){
            errors.rejectValue("username",  "username.matches" ,"Vui lòng nhập đúng định dạng username, (VD: txr@gmail.com)");
            return;
        }
        if(username.trim().replaceAll("\\s+", "").length() < 14 || username.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("username",  "username.length()" ,"User Name phải nằm trong khoảng từ 14 đến 255 kí tự, Vui lòng nhập lại!");
            return;
        }

        if(fullName.trim().isEmpty()){
            errors.rejectValue("fullName",  "fullName.isEmpty" ,"Vui lòng nhập vào fullName, fullName không được để trống");
            return;
        }
        if(fullName.trim().replaceAll("\\s+", "").length() < 3 || fullName.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("fullName",  "fullName.length()" ,"Full Name phải nằm trong khoảng từ 3 đến 255 kí tự, Vui lòng nhập lại!");
            return;
        }
        if(!fullName.matches("^([a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+)$")){
            errors.rejectValue("fullName",  "fullName.matches" ,"Full Name không chứa kí tự đặt biệt, Vui lòng nhập lại theo đúng quy định!");
            return;
        }


        if(password.trim().isEmpty()){
            errors.rejectValue("password",  "password.isEmpty" ,"Vui lòng nhập vào PassWord, PassWord không được để trống");
            return;
        }
        if(password.trim().replaceAll("\\s+", "").length() < 3 || password.trim().replaceAll("\\s+", "").length() > 100){
            errors.rejectValue("password",  "password.length()" ,"PassWord  phải nằm trong khoảng từ 3 đến 50 kí tự, Vui lòng nhập lại!");
            return;
        }

        if(phone.trim().isEmpty()){
            errors.rejectValue("phone",  "phone.isEmpty" ,"Vui lòng nhập vào Phone, Phone không được để trống");
            return;
        }



        if(address.trim().isEmpty()){
            errors.rejectValue("address",  "address.isEmpty" ,"Vui lòng nhập vào Address, Address không được để trống");
            return;
        }
        if(address.trim().replaceAll("\\s+", "").length() < 3 || address.trim().replaceAll("\\s+", "").length() > 50){
            errors.rejectValue("address",  "address.length()" ,"Address  phải nằm trong khoảng từ 3 đến 50 kí tự, Vui lòng nhập lại!");
            return;
        }



    }




    }

