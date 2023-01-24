package com.decagon.rewardyourteacherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Subjects extends BaseClass {

    private String title;

    private long teacherId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subjects subjects = (Subjects) o;
        return getId() != null && Objects.equals(getId(), subjects.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
