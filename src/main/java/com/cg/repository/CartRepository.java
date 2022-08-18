package com.cg.repository;

import com.cg.model.Cart;
import com.cg.model.dto.CartInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT NEW com.cg.model.dto.CartInfoDTO (c.id, c.grandTotal, c.user) FROM Cart c WHERE c.user.id = ?1")

    Optional<CartInfoDTO> findCartInfoDTOByUserId(Long id);

}
