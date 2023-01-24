package com.decagon.rewardyourteacherapi.service;

import com.decagon.rewardyourteacherapi.entity.School;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SchoolService {
    void addSchool(String csvPath);
    ResponseAPI<Map<String, Object>> getAllSchools(Pageable pageable);

    ResponseAPI<List<School>> getAllSchools();
}
