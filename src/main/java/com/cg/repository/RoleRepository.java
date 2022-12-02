package com.cg.repository;

import com.cg.model.Role;
import com.cg.model.dto.RoleDTO;
import com.cg.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Query("SELECT NEW com.cg.model.dto.RoleDTO(" +
                "r.id, " +
                "r.code , " +
                "r.name" +
            ")" +
            "FROM Role AS r"
    )
    List<RoleDTO> findAllRoleDTO();

    Boolean existsByCode(String code);


}
