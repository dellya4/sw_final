package com.example.demo.controller;

import com.example.demo.dto.request.WishGroupRequestDto;
import com.example.demo.dto.response.WishGroupResponseDto;
import com.example.demo.service.impl.WishGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor

public class WishGroupController {

    private final WishGroupService wishGroupService;

    @PostMapping
    public ResponseEntity<WishGroupResponseDto> create(@RequestBody WishGroupRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishGroupService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishGroupResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(wishGroupService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<WishGroupResponseDto>> getMyGroups() {
        return ResponseEntity.ok(wishGroupService.getMyGroups());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
