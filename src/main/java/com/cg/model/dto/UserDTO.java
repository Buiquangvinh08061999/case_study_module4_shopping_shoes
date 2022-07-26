package com.cg.model.dto;

import com.cg.model.User;
import lombok.*;
import lombok.experimental.Accessors;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDTO {

    private Long id;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Trường Email không hợp lệ")
    @Size(min = 10, message = "Email Tối thiểu là 10 đến 50 kí tự")
    @Size(max = 50, message = "Email Tối thiểu là 10 đến 50 kí tự")
    private String username;

    @NotBlank(message = "Password là bắt buộc")
    @Size(min = 8, message = "Password tối thiểu 8 đến 30 kí tự")
    @Size(max = 30, message = "Password tối thiểu 8 đến 30 kí tự")
    private String password;

    @Valid
    private RoleDTO role;


    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role.toRole());
    }

}
