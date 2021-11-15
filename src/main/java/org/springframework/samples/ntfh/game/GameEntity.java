package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends NamedEntity {

    @DateTimeFormat(pattern = ("yyyy/MM/dd"))
    private LocalDate date;
    private Timestamp startTime;
    private Timestamp finishTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Comment> comments;
    private Boolean spectatorsAllowed;

    public Long getDuration() {
        return finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
    }

}
