package org.springframework.samples.ntfh.achievement;

import java.util.Date;
import java.util.function.Predicate;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievements")
public class Achievement extends NamedEntity{
    
    private String name;
    private Date date;
    private Predicate achievementCondition;

}
