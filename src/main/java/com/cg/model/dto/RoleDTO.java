package com.cg.model.dto;

import com.cg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleDTO implements Validator {

    private Long id;
    private String code;
    private String name;


    public Role toRole() {
        return new Role()
                .setId(id)
                .setCode(code)
                .setName(name);
    }



    @Override
    public boolean supports(Class<?> aClass) {
        return RoleDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleDTO roleDTO = (RoleDTO) target;
        String code = roleDTO.getCode();

        if(code.trim().isEmpty()){
            errors.rejectValue("code",  "code.isEmpty" ,"Vui lòng nhập vào tên danh mục, tên danh mục không được để trống");
            return;
        }
        /*Không tính khoảng trắng dấu cách, phương thức  xử lí dấu cách hay của a Phôn*/
        if(code.trim().replaceAll("\\s+", "").length() < 3 || code.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("code",  "title.length" ,"Tên phải nằm trong khoảng từ 3 Đến 255 Ký Tự");
            return;
        }
        if(!code.matches("^([a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+)$")){
            errors.rejectValue("code",  "code.matches" ,"Không được có kí tự đặt biệt, Vui lòng nhập tên đúng quy định!");
            return;
        }
     }
}
