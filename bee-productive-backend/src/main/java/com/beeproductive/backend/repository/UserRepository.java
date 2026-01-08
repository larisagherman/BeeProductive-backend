package com.beeproductive.backend.repository;

import com.beeproductive.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFireBaseId(String fireBaseId);
}
