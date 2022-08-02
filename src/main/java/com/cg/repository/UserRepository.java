package com.cg.repository;

import com.cg.model.User;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);

//    @Query("SELECT NEW com.cg.model.dto.UserDTO (u.id, u.username) FROM User u WHERE u.username = ?1")
//    Optional<UserDTO> findUserDTOByUsername(String username);

    /*Hiển thị list danh sách ra*/
    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username, u.password , u.fullname , u.phone , u.locationRegion, u.role) FROM User AS u ")
    List<UserDTO> findAllUserDTO();


    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username , u.fullname , u.phone, u.locationRegion, u.role) FROM User AS u JOIN LocationRegion AS loca ON loca.id = u.locationRegion.id JOIN Role AS r ON r.id = u.role.id WHERE CONCAT(u.id, u.username, u.fullname , u.phone, loca.provinceName, loca.districtName, loca.wardName, loca.address, r.code) LIKE %?1% ")
    List<UserDTO> search(String keywork);


//    @Query("SELECT NEW com.cg.model.dto.UserDTO(u.id, u.username , u.fullname , u.phone) FROM User AS u  WHERE CONCAT(u.id, u.username , u.fullname , u.phone) LIKE %?1% ")
//    List<UserDTO> search(String keywork);


    Boolean existsByUsernameAndIdNot(String username, Long id);

}
