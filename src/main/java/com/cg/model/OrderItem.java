package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
@Accessors(chain = true)
public class OrderItem extends BaseProduct{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "total_price")
    private BigDecimal totalPrice;


    /*Số 2*/
    /*Làm cách này với quan hệ một nhiều*/
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
