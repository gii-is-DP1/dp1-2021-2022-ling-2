package org.springframework.samples.ntfh.entity.player;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.entity.game.Game;
import org.springframework.samples.ntfh.entity.model.BaseEntity;
import org.springframework.samples.ntfh.entity.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player extends BaseEntity {
    @NotNull
    private Integer glory;

    @NotNull
    private Integer kills;

    @NotNull
    private Integer gold;

    @NotNull
    private Integer wounds;

    @NotNull
    @ManyToOne() // TODO cascade types?
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user; // User who is handling this player

    // Should not change when user's character is changed. Once the
    // row is created in the databse, it stays the same
    @ManyToOne(optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id")
    private Character characterType;

    /**
     * Derivated from User, who has a "game" column with the game where he/she is
     * playing in.
     * 
     * @author andrsdt
     * @return game where the player is playing in.
     */
    @Transient
    public Game getGame() {
        return user.getGame();
    }
}
