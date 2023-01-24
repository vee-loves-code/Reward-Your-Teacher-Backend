package com.decagon.rewardyourteacherapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification extends BaseClass {

    private String messageBody;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_Id" , referencedColumnName = "id")
    private User user;

}
