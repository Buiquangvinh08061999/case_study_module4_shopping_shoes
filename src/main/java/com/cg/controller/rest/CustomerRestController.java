package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Role;
import com.cg.model.User;
import com.cg.model.dto.*;
import com.cg.service.role.IRoleService;
import com.cg.service.user.IUserService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/customers")

public class CustomerRestController {

    @Autowired
    private AppUtil appUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllUserDTO(){

        List<UserDTO> userDTO = userService.findAllUserDTOByDeletedIsFalse();

        return new ResponseEntity<>(userDTO , HttpStatus.OK);

    }


    @PostMapping("/search")
    public ResponseEntity<?> getSearchUserDTO(@RequestBody SearchDTO searchDTO, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        String keySearch = searchDTO.getKeySearch();
        keySearch = "%" + keySearch + "%";

        System.out.println(keySearch);
        List<UserDTO> userDTO = userService.searchAllUser(keySearch);

        if(userDTO.isEmpty()){
            throw new DataInputException("Không tìm thấy từ khóa(  " + searchDTO.getKeySearch() + "   )vui lòng nhập lại!"); /*nếu từ khóa tìm kiếm rỗng sẽ bắn ra lỗi*/
        }

        return new ResponseEntity<>(userDTO , HttpStatus.OK);/*nếu thành công trả về 1 danh sách userDTO*/
    }


    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult){

        new UserDTO().validate(userDTO , bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        userDTO.setId(0L);
        userDTO.getLocationRegion().setId(0L);

        userDTO.setUrlImage("avatar.png");  /*mặc định đường dẫn ảnh chứa trong thư mục*/

        Boolean existsById  = userService.existsById(userDTO.getId());
        if (existsById) {
            throw new EmailExistsException("ID đã tồn tại vui lòng nhập lại!");
        }

        Boolean existsByUsername  = userService.existsByUsername(userDTO.getUsername());
        if (existsByUsername) {
            throw new EmailExistsException("Username "+ userDTO.getUsername()+ " đã tồn tại.Vui lòng nhập lại!");
        }

        Boolean existsByPhone = userService.existsByPhone(userDTO.getPhone());
        if (existsByPhone) {
            throw new EmailExistsException("Phone "+ userDTO.getPhone() +" đã tồn tại.Vui lòng nhập lại");
        }

        Optional<Role> roleId = roleService.findById(userDTO.getRole().getId());
        if(!roleId.isPresent()){
            throw new EmailExistsException("ID ROLE không tồn tại,vui lòng không được chỉnh sửa");
        }


        try{
            User user = userDTO.toUser();
            User newUser = userService.save(user);   /*trả về 1 thằng mới, nên ra phải User newUser đối tượng ra*/

            return new ResponseEntity<>(newUser.toUserDTO(),  HttpStatus.CREATED); /*trả về dữ liệu lưu lại User, nhưng sẽ chuyển qua UserDTO để xử ls, set tất cả dữ liệu User, về UserDTO*/

        }catch (DataIntegrityViolationException e){
            throw new DataInputException("Thông tin tài khoản không hợp lệ ");
        }

    }

    /*Hàm hiển thị dữ liệu Edit theo id, tìm theo id của userId, ở phần ajax đó, ta đẩy userId đó vào đây, để đổ tất cả dữ liệu các trường về*/
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {

        Optional<User> userOptional = userService.findById(id);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        return new ResponseEntity<>(userOptional.get().toUserDTO(),  HttpStatus.OK);  /*ở hàm toUserDTO ta đã set lại các giá trị cần lấy ra, muốn lấy trường nào thí lấy, Bỏ qua các giá trị ngày tạo, ngày cập nhât,*/
    }


    @PutMapping("/update")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> doUpdate(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult){
        new UserDTO().validate(userDTO, bindingResult);

        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsById = userService.existsById(userDTO.getId());
        if(!exitsById){
            throw new EmailExistsException("ID không tồn tại!");
        }

        Boolean exitsByUserName = userService.existsByUsernameAndIdIsNot(userDTO.getUsername(), userDTO.getId());
        if(exitsByUserName){
            throw new EmailExistsException("Username "+ userDTO.getUsername()+ " đã tồn tại.Vui lòng nhập lại!");
        }

        Boolean existsByPhone = userService.existsByPhoneAndIdIsNot(userDTO.getPhone(), userDTO.getId());
        if (existsByPhone) {
            throw new EmailExistsException("Phone "+ userDTO.getPhone() +" đã tồn tại.Vui lòng nhập lại");
        }

        Optional<Role> roleId = roleService.findById(userDTO.getRole().getId());
        if(!roleId.isPresent()){
            throw new EmailExistsException("ID ROLE không tồn tại!");
        }

        userDTO.getLocationRegion().setId(0L);


        try {

            User user = userDTO.toUser();
            User updateUser = userService.saveUpdate(user);

            return new ResponseEntity<>(updateUser.toUserDTO(), HttpStatus.ACCEPTED);

        }catch (DataInputException e){
            throw new DataInputException("Thông tin tài khoản không hợp lệ");
        }
    }

    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllUserDTO(@PathVariable Long id){

       Optional<User> user = userService.findById(id);

       if(user.isPresent()){

            user.get().setDeleted(true);

            userService.save(user.get());

           return new ResponseEntity<>(HttpStatus.ACCEPTED);

       }else {
           throw new DataInputException("Thông tin không hợp lệ");
       }

    }

    @GetMapping("/role")
    public ResponseEntity<?> getAllRoleDTO(){

        List<RoleDTO> roleDTO = roleService.findAllRoleDTO();

        return new ResponseEntity<>(roleDTO,HttpStatus.OK);

    }
    @PostMapping("/role/create")
    public ResponseEntity<?> doCreateRoleDTO(@Validated @RequestBody RoleDTO roleDTO, BindingResult bindingResult){

        new RoleDTO().validate(roleDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        roleDTO.setId(0L);
        roleDTO.setName("ROLE");

        Boolean exitsById = roleService.existsById(roleDTO.getId());
        if(exitsById){
            throw new EmailExistsException("Role đã tồn tại Id này");
        }

        Boolean exitsByCode = roleService.existsByCode(roleDTO.getCode());
        if(exitsByCode){
            throw new EmailExistsException("Tên "+ roleDTO.getCode() +" của danh mục code của Role đã tồn tại");
        }

        try {
            Role roleSave = roleService.save(roleDTO.toRole());

            return new ResponseEntity<> (roleSave.toRoleDTO(), HttpStatus.CREATED);
        }catch (DataInputException e){
            throw new DataInputException("Thông tin role không hợp lệ");
        }

    }

    /*Sắp xếp theo id tăng dần từ 1->99*/
    @GetMapping("/sortASCId")
    public ResponseEntity<?> getAllSortASCIdUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortASCIdUserDTO();

        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/sortDESCId")
    public ResponseEntity<?> getAllSortDESCIdUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortDESCIdUserDTO();

        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /*Sắp xếp theo username tăng dần từ A->Z*/
    @GetMapping("/sortASCUserName")
    public ResponseEntity<?> getAllSortASCUserNameUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortASCUserNameUserDTO();


        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/sortDESCUserName")
    public ResponseEntity<?> getAllSortDESCUserNameUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortDESCUserNameUserDTO();


        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /*Sắp xếp theo fullName tăng dần từ A->Z*/
    @GetMapping("/sortASCFullName")
    public ResponseEntity<?> getAllSortASCFullNameUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortASCFullNameUserDTO();


        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/sortDESCFullName")
    public ResponseEntity<?> getAllSortDESCFullNameUserDTO() {

        List<UserDTO> userDTO = userService.findAllSortDESCFullNameUserDTO();

        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    /*Đếm số lượng sản phẩm*/
    @GetMapping("/count")
    public ResponseEntity<?> getAllCountUserDTO() {
        CountDTO countDTO = userService.findAllCount();

        return new ResponseEntity<>(countDTO, HttpStatus.OK);

    }

    /*Trả về tập danh sách đã bị xóa , hiển thị trong history User*/
    @GetMapping("/historyUser")
    public ResponseEntity<?> getAllHistoryUserDTO() {

        List<UserDTO> userDTO = userService.findAllUserDTOHistoryByDeletedIsTrue();

        /*Nếu rỗng bắn ra lỗi*/
        if (userDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    //Hàm khôi phục theo id, chỉ gần set user.get().setDeleted(flase);
    @PostMapping("/restore/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> restoreAllUserDTO(@PathVariable Long id){

        Optional<User> user = userService.findById(id);

        if(user.isPresent()){

            user.get().setDeleted(false);

            userService.save(user.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        }else {
            throw new DataInputException("Thông tin không hợp lệ");
        }

    }






































}
