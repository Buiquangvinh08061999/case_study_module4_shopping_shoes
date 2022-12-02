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


    List<UserDTO> findAllUserDTOByDeletedIsFalse();

    /*Search tất cả các trường */
    List<UserDTO> searchAllUser(String keyword);

    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    /*Dùng để truyền vào menu(show_cart)*/
    Optional<UserDTO> findUserDTOByUsername(String username);


    Optional<UserDTO> findUserDTOById(long id);


    Boolean existsById(Long id);
    Boolean existsByUsername(String username);
    Boolean existsByPhone(String phone);

    /*Dùng cho saveupdate*/
    User saveUpdate(User user);

    //Dùng cho update,
    Boolean existsByUsernameAndIdIsNot(String username, Long id);
    Boolean existsByPhoneAndIdIsNot(String phone, Long id);



    List<UserDTO> findAllSortASCIdUserDTO();

    List<UserDTO> findAllSortDESCIdUserDTO();

    List<UserDTO> findAllSortASCUserNameUserDTO();

    List<UserDTO> findAllSortDESCUserNameUserDTO();

    List<UserDTO> findAllSortASCFullNameUserDTO();

    List<UserDTO> findAllSortDESCFullNameUserDTO();

    CountDTO findAllCount();

    List<UserDTO> findAllUserDTOHistoryByDeletedIsTrue();


}
