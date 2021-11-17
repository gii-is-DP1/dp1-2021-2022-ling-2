package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GameEntity extends NamedEntity {

    private Timestamp startTime;
    private Timestamp finishTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Comment> comments;
    //Lista de usuarios en el juego [Mirar!!!!!!!]
    //@OneToMany(cascade = CascadeType.ALL,mappedBy = "game")
    //private Set<User> players;
    private Boolean spectatorsAllowed;

    public Long getDuration() {
        return finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
    }

}
