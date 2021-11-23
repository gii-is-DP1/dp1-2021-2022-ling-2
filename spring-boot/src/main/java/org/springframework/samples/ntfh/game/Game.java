package org.springframework.samples.ntfh.game;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

    @NotNull // Set by the server to Time.now()
    private Long startTime; // unix timestamp

    // Set by the server when the game finished. null meanwhile
    private Long finishTime; // unix timestamp

    @NotNull // Set from Lobby
    private Boolean hasScenes;

    // TODO hacer la asociacion tambien desde parte de comments? bidireccional?
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL) // Deleting the game will delete the comments
    @JsonIgnore
    private Set<Comment> comments;

    // Set from Lobby by creating Players instances from users
    @ManyToMany // TODO cascade? If we set CascadeType.ALL then deleting the game will delete
                // the players. It shouldn't be like that.
    private Set<Player> players;

    @ManyToOne
    private Player leader;

    @ManyToOne
    private Player winner;

    /**
     * @author andrsdt
     */
    @Transient
    public Boolean getHasFinished() {
        return finishTime != null;
    }

    /**
     * Derived. Returns the duration of the game in seconds
     * 
     * @author andrsdt
     * @return Long duration of the time in seconds
     */
    @Transient
    public Long getDuration() {
        if (finishTime == null)
            return null; // To avoid NullPointerException if the game hasn't finished
        Long timeInMilliseconds = finishTime - startTime;
        return timeInMilliseconds / 1000;
    }
}
