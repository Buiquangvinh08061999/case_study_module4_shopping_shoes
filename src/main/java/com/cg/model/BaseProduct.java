package com.cg.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter

@MappedSuperclass

public abstract class BaseProduct extends BaseEntities{
    @Column(nullable = false)
    private String title;

    @Digits(integer = 12, fraction = 0)
    private BigDecimal price;

    private int quantity;
}
