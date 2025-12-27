package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.Wish;
import com.example.demo.enums.WishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findAllByStatus(WishStatus status);

    List<Wish> findAllByOwner(User owner);

    List<Wish> findAll();
}
