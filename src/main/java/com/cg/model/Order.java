package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@Accessors(chain = true)

public class Order extends BaseEntities{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @CreationTimestamp
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "delivery_date")
    private String deliveryDate;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "is_pending",columnDefinition = "boolean default true")
    private boolean isPending;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    /*Số 2*/
    /*Làm cách này với quan hệ một nhiều*/
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;


    @OneToOne
    @JoinColumn(name ="location_region_delivery_id", nullable = false)
    private LocationRegionDelivery locationRegionDelivery;



}
