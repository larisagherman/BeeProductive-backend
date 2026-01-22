package com.beeproductive.backend.repository;

import com.beeproductive.backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
    Optional<Group> findByCode(String code);
}
