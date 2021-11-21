package org.springframework.samples.ntfh.game;

import java.security.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;
import org.springframework.samples.ntfh.comments.Comment;
import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pabrobcam
 * @author andrsdt
 */
@Getter
@Setter
@Entity
@Table(name = "games")
public class Game extends BaseEntity {

    @NotNull // Set from Lobby
    private String name;

    @NotNull // Set by the server to Time.now()
    private Timestamp startTime;

    // Set by the server when the game finished. null meanwhile
    private Timestamp finishTime;

    @NotNull // Set from Lobby
    private Boolean hasScenes;

    // TODO hacer la asociacion tambien desde parte de comments? bidireccional?
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    @JsonIgnore
    private Set<Comment> comments;

    // Set from Lobby by creating Players instances from users
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Player> players;

    @Transient
    private Boolean hasFinished;

    @Transient
    private Long duration;

    /**
     * @author andrsdt
     */
    public Boolean hasFinished() {
        return finishTime != null;
    }

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    public Long getDuration() {
        if (hasFinished == null)
            return null; // To avoid NullPointerException if the game hasn't finished
        Long timeInMilliseconds = finishTime.getTimestamp().getTime() - startTime.getTimestamp().getTime();
        return timeInMilliseconds / 1000;
    }
}
