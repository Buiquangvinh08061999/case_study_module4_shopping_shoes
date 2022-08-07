package com.cg.service.user;

import com.cg.model.User;
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

    List<UserDTO> searchAllUser(String keywork);


    User getByUsername(String username);

    Optional<User> findByUsername(String username);

//    Optional<UserDTO> findUserDTOByUsername(String username);

    /*Update tìm theo ID*/
    Boolean existsById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);

    Boolean existsByUsernameAndIdNot(String username, Long id);

    User saveUpdate(User user);
}
