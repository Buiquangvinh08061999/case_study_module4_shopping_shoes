package com.cg.service.role;

import com.cg.model.Role;
import com.cg.model.dto.RoleDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);

    /*viết api để hiển thị tất cả thông tin*/
    List<RoleDTO> findAllRoleDTO();


    Boolean existsById(Long id);

    Boolean existsByCode(String code);




}
