package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game extends BaseEntity {

    // Note: we should consider dd/MM/yyyy HH:mm:ss format since a game can start
    // today and end tomorrow
    // @DateTimeFormat(pattern = "HH/mm/ss")
    private Timestamp startTime;

    // Note: we should consider dd/MM/yyyy HH:mm:ss format since a game can start
    // today and end tomorrow
    // @DateTimeFormat(pattern = "HH/mm/ss")
    private Timestamp finishTime;

    // The set of comments would be fetched from another table. To be implemented
    // yet.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Comment> comments;

    private Boolean spectatorsAllowed;

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    public Long getDuration() {
        return finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
    }
}
