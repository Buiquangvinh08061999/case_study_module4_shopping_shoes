package com.cg.controller.rest;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Role;
import com.cg.model.User;
import com.cg.model.dto.UserDTO;
import com.cg.service.role.IRoleService;
import com.cg.service.user.IUserService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/search/{word}")
    public ResponseEntity<?> getSearchUserDTO(@PathVariable String word){

        List<UserDTO> userDTO = userService.searchAllUser(word);

        if(userDTO.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(userDTO , HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult){

//        new UserDTO().validate(userDTO , bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        userDTO.setId(0L);
        userDTO.getLocationRegion().setId(0L);
        userDTO.setUrlImage("user.png");

        Boolean existsById  = userService.existsById(userDTO.getId());
        if (existsById) {
            throw new EmailExistsException("ID đã tồn tại vui lòng nhập lại!");
        }

        Boolean existsByUsername  = userService.existsByUsername(userDTO.getUsername());
        if (existsByUsername) {
            throw new EmailExistsException("Username đã tồn tại vui lòng nhập lại!");
        }

        Boolean existsByPhone = userService.existsByPhone(userDTO.getPhone());
        if (existsByPhone) {
            throw new EmailExistsException("Phone đã tồn tại");
        }

        Optional<Role> role = roleService.findById(userDTO.getRole().getId());

        if(!role.isPresent()){
            throw new EmailExistsException("ID ROLE không tồn tại!");
        }

        try{
            User user = userDTO.toUser();
            User newUser = userService.save(user);

            return new ResponseEntity<>(newUser.toUserDTO(), HttpStatus.CREATED);

        }catch (DataIntegrityViolationException e){
            throw new DataInputException("Thông tin tài khoản không hợp lệ ");
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> doUpdate(@RequestBody UserDTO userDTO, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsById = userService.existsById(userDTO.getId());
        if(!exitsById){
            throw new EmailExistsException("ID không tồn tại!");
        }

//        Boolean exitsByUserName = userService.existsByUsername(userDTO.getUsername());
//        if(exitsByUserName){
//            throw new EmailExistsException("UserName đã tồn tại!");
//        }
//
//        Boolean existsByPhone = userService.existsByPhone(userDTO.getPhone());
//        if (existsByPhone) {
//            throw new EmailExistsException("Phone đã tồn tại");
//        }
//
//        Optional<Role> roleId = roleService.findById(userDTO.getRole().getId());
//        if(!roleId.isPresent()){
//            throw new EmailExistsException("ID ROLE không tồn tại!");
//        }
        userDTO.getLocationRegion().setId(0L);

        try {
            User updateUser = userService.saveUpdate(userDTO.toUser());
            return new ResponseEntity<>(updateUser.toUserDTO(), HttpStatus.ACCEPTED);

        }catch (DataIntegrityViolationException e){
            throw new DataInputException("Thông tin tài khoản không hợp lệ");
        }
    }

    //Hàm xóa mềm theo id
    @DeleteMapping("/delete/{id}")
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {
        Optional<User> userOptional = userService.findById(id);

        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        return new ResponseEntity<>(userOptional.get().toUserDTO(),  HttpStatus.OK);
    }

}
