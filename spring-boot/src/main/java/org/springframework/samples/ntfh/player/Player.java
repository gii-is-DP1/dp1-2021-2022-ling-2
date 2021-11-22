package org.springframework.samples.ntfh.player;

import java.beans.Transient;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players")
public class Player extends BaseEntity {
    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer glory;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer kills;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer gold;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer wounds;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user; // User who is handling this player

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
