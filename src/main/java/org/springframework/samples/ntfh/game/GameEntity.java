package org.springframework.samples.ntfh.game;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends NamedEntity {

    private Integer duration;
    @DateTimeFormat(pattern = ("yyyy/MM/dd"))
    private LocalDate date;
    @DateTimeFormat(pattern = ("HH/mm/ss"))
    private LocalTime startTime;
    @DateTimeFormat(pattern = ("HH/mm/ss")) 
    private LocalTime finishTime;
    private String comments;
    private Boolean spectatorsAllowed;


}
