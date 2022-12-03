package com.cg.model.dto;

import com.cg.model.Category;
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
public class CategoryDTO implements Validator {

    private Long id;
    private String title;

    public Category toCategory() {
        return new Category()
                .setId(id)
                .setTitle(title);
    }













    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDTO categoryDTO = (CategoryDTO)target;

        String title = categoryDTO.getTitle();
        /*Không tính khoảng trắng dấu cách, trim() chỉ xóa bỏ khoảng trống 2 đầu*/
        if ((title.trim()).isEmpty()) {
            errors.rejectValue("title",  "title.isEmpty" ,"Vui lòng nhập vào tên danh mục, tên danh mục không được để trống");
            return;
        }

        /*Không tính khoảng trắng dấu cách, phương thức  xử lí dấu cách hay của a Phôn*/
        if(title.trim().replaceAll("\\s+", "").length() < 3 || title.trim().replaceAll("\\s+", "").length() > 255){
            errors.rejectValue("title",  "title.length" ,"Tên phải nằm trong khoảng từ 3 Đến 255 Ký Tự");
            return;
        }

        if(!title.matches("^([a-zA-Z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+)$")){
            errors.rejectValue("title",  "title.matches" ,"Không được có kí tự đặt biệt, Vui lòng nhập tên đúng định!");
            return;
        }

    }


}
