package com.cg.model;

import com.cg.model.dto.CartInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
@Accessors(chain = true)

public class Cart extends BaseEntities{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems;


    public CartInfoDTO toCartInfoDTO() {
        return new CartInfoDTO()
                .setId(id)
                .setGrandTotal(grandTotal.toString())
                .setUser(user.toUserDTO());
    }
}
