package com.decagon.rewardyourteacherapi.entity;

import com.decagon.rewardyourteacherapi.enums.Roles;
import com.decagon.rewardyourteacherapi.enums.SchoolType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "teacher")
public class Teacher extends User{

    private String position;
    private String periodOfTeaching;
    private int yearsOfTeaching;
    private boolean isRetired;

    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @OneToMany
    @JoinColumn(name = "teacherId")
    private List<Subjects> subjectsList;

}
