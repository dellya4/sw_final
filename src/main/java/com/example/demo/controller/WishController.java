package com.example.demo.controller;

import com.example.demo.dto.request.WishRequestDto;
import com.example.demo.dto.response.WishResponseDto;
import com.example.demo.service.impl.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor

public class WishController {

    private final WishService wishService;

    @PostMapping
    public ResponseEntity<WishResponseDto> create(@RequestBody WishRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishService.create(dto));
    }

    @GetMapping("/published")
    public ResponseEntity<List<WishResponseDto>> getAllPublished() {
        return ResponseEntity.ok(wishService.getAllPublished());
    }

    @GetMapping("/my")
    public ResponseEntity<List<WishResponseDto>> getMyWishes() {
        return ResponseEntity.ok(wishService.getMyWishes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishResponseDto> update(@PathVariable Long id,
                                                  @RequestBody WishRequestDto dto) {
        return ResponseEntity.ok(wishService.update(id, dto));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id) {
        wishService.publish(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
