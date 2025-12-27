package com.example.demo.dto.response;

import com.example.demo.enums.WishStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class WishResponseDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private WishStatus status;
    private String ownerUsername;
    private String categoryName;
    private String groupTitle;
}
