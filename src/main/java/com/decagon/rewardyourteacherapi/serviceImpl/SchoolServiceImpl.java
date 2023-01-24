package com.decagon.rewardyourteacherapi.serviceImpl;

import com.decagon.rewardyourteacherapi.dto.SchoolResponseDto;

import com.decagon.rewardyourteacherapi.entity.School;
import com.decagon.rewardyourteacherapi.repository.SchoolRepository;
import com.decagon.rewardyourteacherapi.response.ResourceNotFoundException;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import com.decagon.rewardyourteacherapi.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.decagon.rewardyourteacherapi.utils.ListOfSchoolUtil.readAllSchoolsFromCsvFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
   // @PostConstruct
    public void addSchool(String csvPath) {
        List<SchoolResponseDto>schoolsToBeAdded = readAllSchoolsFromCsvFile(csvPath);
        List<School> schoolsInDatabase = schoolRepository.findAll();
        if(schoolsToBeAdded.size() > schoolsInDatabase.size()){
            schoolRepository.deleteAll();
            schoolsToBeAdded.forEach(school-> {
                schoolRepository.save(new School(school.getName(),school.getType(),school.getAddress(),school.getCity(), school.getState()));
            });
            log.info("Schools  Added");
        }else{
            log.info("Schools Already  Added");
        }
    }

    @Override
    public ResponseAPI<Map<String, Object>> getAllSchools(Pageable pageable) {

        List<School> schoolist;
        Pageable pageOne = PageRequest.of(0, 5, Sort.by("name").ascending());
        Page<School> schoolPage;
        schoolPage = schoolRepository.findAll(pageOne);
        schoolist = schoolPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("schools", schoolist);
        response.put("currentPage", schoolPage.getNumber());
        response.put("totalItems", schoolPage.getTotalElements());
        response.put("totalPages", schoolPage.getTotalPages());
        return new ResponseAPI<>("success" , LocalDateTime.now() , response);


        //return new ResponseAPI<>("success", LocalDateTime.now(), schoolRepository.findAll(pageable).getContent());
    }

    @Override
    public ResponseAPI<List<School>> getAllSchools() {
        List<School> schoolList = schoolRepository.findAll();
        return new ResponseAPI<>("success" , LocalDateTime.now() , schoolList);
    }

    public School findSchoolByName(String name){
        return  schoolRepository.findAllByName(name).orElseThrow(()-> new ResourceNotFoundException(name + " not found"));
    }

}
