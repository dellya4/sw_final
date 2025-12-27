package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.WishGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository

public interface WishGroupRepository extends JpaRepository<WishGroup, Long> {

    List<WishGroup> findAllByOwner(User user);
}
