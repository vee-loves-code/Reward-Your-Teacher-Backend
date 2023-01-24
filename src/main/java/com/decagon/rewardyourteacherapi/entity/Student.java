package com.decagon.rewardyourteacherapi.entity;

import com.decagon.rewardyourteacherapi.enums.Roles;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue(value = "student")
public class Student extends User {

}
