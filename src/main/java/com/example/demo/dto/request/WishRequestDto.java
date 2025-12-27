package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class WishRequestDto {
    private String title;
    private String description;
    private double price;
    private Long categoryId;
    private Long wishGroupId;
}
