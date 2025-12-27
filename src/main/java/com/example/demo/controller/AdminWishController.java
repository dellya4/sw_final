package com.example.demo.controller;

import com.example.demo.dto.response.WishResponseDto;
import com.example.demo.service.impl.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/wishes")
@RequiredArgsConstructor
public class AdminWishController {

    private final WishService wishService;

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getAll() {
        return ResponseEntity.ok(wishService.getAllForModeration());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(wishService.getById(id));
    }
}