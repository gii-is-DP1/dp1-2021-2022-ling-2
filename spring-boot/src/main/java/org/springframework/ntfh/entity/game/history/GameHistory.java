package org.springframework.ntfh.entity.game.history;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;
import org.springframework.ntfh.entity.comment.Comment;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.Getter;

/**
 * This table will store data about games that have already finished and want to
 * be persisted. This way we can debloat the Game table and only keep the
 * important attributes for ingame interactions, as well as stablishing a
 * separation between Ingame and Past that will be helpful for statistics
 * computations and the implementation of related User Stories.
 * 
 * @author andrsdt
 */
@Getter
@Entity
@Table(name = "games_history")
public class GameHistory extends BaseEntity {
    // Foreign key to the game it refers to
    @NotNull
    @OneToOne
    private Game game;

    @NotNull // NotNull since the rows for this enty will be created will be created with the
             // final results of a game
    private Long finishTime; // unix timestamp

    @OneToOne
    private Player winner;

    // TODO hacer la asociacion tambien desde parte de comments? bidireccional?
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    // orphanRemoval: The "comment" rows will be deleted when the game is deleted
    @JsonIgnore
    private Set<Comment> comments;

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
        Long timeInMilliseconds = finishTime - game.getStartTime();
        return timeInMilliseconds / 1000;
    }

}
