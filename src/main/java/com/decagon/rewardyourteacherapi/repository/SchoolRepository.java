package com.decagon.rewardyourteacherapi.repository;

import com.decagon.rewardyourteacherapi.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findAllByName(String name);
}
