package com.cg.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CountDTO {
    private Long count;

    public CountDTO(Long count) {
        this.count = count;
    }

}
