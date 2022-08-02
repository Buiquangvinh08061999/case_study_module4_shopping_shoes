package com.cg.model.dto;

import com.cg.model.LocationRegion;
import com.cg.model.Role;
import com.cg.model.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDTO{

    private Long id;

    @NotBlank(message = "Username là bắt buộc")
//    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,3}$", message = "Vui lòng nhập đúng định dạng UserName VD(txr@gmail.com)")
    @NotEmpty(message = "Username không được để trống")
    @Email(message = "Vui lòng nhập đúng định dạng UserName VD(txr@gmail.com)")
    @Size(min = 13, message = "Username tối thiểu 13 đến 30 kí tự")
    @Size(max = 30, message = "Username tối thiểu 13 đến 30 kí tự")
    private String username;


    @NotBlank(message = "Password là bắt buộc")
    @NotEmpty(message = "Password không được để trống")
    @Size(min = 3, message = "Password tối thiểu 3 đến 30 kí tự")
    @Size(max = 30, message = "Password tối thiểu 3 đến 30 kí tự")
    private String password;


    @NotBlank(message = "FullName là bắt buộc")
    @NotEmpty(message = "FullName không được để trống")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "FullName phải là chữ , không có kí tự đặt biệt và số")
    private String fullname;


    @NotBlank(message = "Phone là bắt buộc")
    @NotEmpty(message = "Phone không được để trống")
    @Pattern(regexp = "^[0][1-9][0-9]{8,9}|[+84][1-9][0-9]{10,11}$", message = "Phone không bao gồm dấu cách,chữ,kí tự đặc biệt,Phone gồm 10 đến 11 số và bắt đầu là số 0 và +84")
    private String phone;


    @Valid
    private LocationRegionDTO locationRegion;

    @Valid
    private RoleDTO role;


    public UserDTO(Long id, String username, String password, String fullname, String phone, LocationRegion locationRegion, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.role = role.toRoleDTO();

    }
    //Search theo trường bỏ ẩn password
    public UserDTO(Long id, String username, String fullname, String phone, LocationRegion locationRegion, Role role) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.role = role.toRoleDTO();

    }

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    public UserDTO(Long id, String username,String fullname, String phone) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setFullname(fullname)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion())
                .setRole(role.toRole());
    }

//    @Override
//    public boolean supports( Class<?> clazz) {
//        return UserDTO.class.isAssignableFrom(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        UserDTO userDTO = (UserDTO) target;
//
//        String userNameStr = userDTO.getUsername();
//        String fullNameStr = userDTO.getFullname();
//
//        if(!userNameStr.matches("(^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,3}$)")){
//            errors.rejectValue("username", "userNameStr.matches","Vui lòng nhập đúng định dạng UserName VD(txr@gmail.com)");
//            return;
//        }
//        if(userNameStr.isEmpty()) {
//            errors.rejectValue("username", "userNameStr.isEmpty", "UserName không được để trống");
//            return;
//        }
//        if(userNameStr == null){
//            errors.rejectValue("username", "userNameStr.null","UserName không được null");
//            return;
//        }
//        if(userNameStr.length() < 6 || userNameStr.length() > 31){
//            errors.rejectValue("username", "userNameStr.length","UserName từ 6 đến 31 kí tự");
//            return;
//        }

//        if(!fullNameStr.matches(("^[a-zA-Z]$"))){
//            errors.rejectValue("fullname", "fullNameStr.matches","FullName không có kí tự đặt biệt và số");
//            return;
//        }
//
//        if(fullNameStr.isEmpty()){
//            errors.rejectValue("fullname", "fullNameStr.isEmpty","FullName không được rỗng");
//            return;
//        }


    }

