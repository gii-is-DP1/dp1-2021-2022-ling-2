package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Entity
public class GameEntity extends NamedEntity {

    private Integer duration;
    @DateTimeFormat (pattern = "yyyy/MM/dd")
    private LocalDate date;
    @DateTimeFormat (pattern = "HH/mm/ss")
    private LocalTime stratTime;
    @DateTimeFormat (pattern = "HH/mm/ss")
    private LocalTime finishTime;
    private String comments;
    private Boolean spectatorsAllowed;
    
}
