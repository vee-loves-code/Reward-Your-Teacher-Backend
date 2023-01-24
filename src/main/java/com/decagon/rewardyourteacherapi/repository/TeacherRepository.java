package com.decagon.rewardyourteacherapi.repository;

import com.decagon.rewardyourteacherapi.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher , Long> {
    List<Teacher> findAllByName(String name);

   Page<Teacher> findAllBySchool(String school, Pageable pageable);

    Optional<Teacher> findTeacherByEmail(String email);

    Page<Teacher> findAll(Pageable pageable);
}


