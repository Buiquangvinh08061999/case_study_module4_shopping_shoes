package com.cg.model;

import com.cg.model.dto.OrderStatusDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_status")
@Accessors(chain = true)
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;


    @OneToMany(targetEntity = Order.class, mappedBy = "orderStatus" , fetch = FetchType.EAGER)
    private Set<Order> orders;


    public OrderStatusDTO toOrderStatusDTO() {
        return new OrderStatusDTO()
                .setId(id)
                .setTitle(title);
    }
}
