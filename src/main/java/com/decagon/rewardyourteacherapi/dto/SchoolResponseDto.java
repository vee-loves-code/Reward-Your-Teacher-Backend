package com.decagon.rewardyourteacherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SchoolResponseDto {

    private String name;
    private String type;
    private String address;
    private String city;
    private String state;

}
