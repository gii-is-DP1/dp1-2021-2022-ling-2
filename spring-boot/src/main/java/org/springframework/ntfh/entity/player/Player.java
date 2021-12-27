package org.springframework.ntfh.entity.player;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.user.User;

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
    private Integer guard;

    @NotNull
    private Integer turnOrder; // Order in which the player will take their turn. The leader will be 0 (first)

    @NotNull
    @ManyToOne() // TODO cascade types?
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private User user; // User who is handling this player

    // Should not change when user's character is changed. Once the
    // row is created in the databse, it stays the same
    @ManyToOne(optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id")
    private Character characterType;

    @OneToMany
    private List<AbilityCardIngame> hand = new ArrayList<>();

    @OneToMany
    private List<AbilityCardIngame> abilityPile = new ArrayList<>();

    @OneToMany
    private List<AbilityCardIngame> discardPile = new ArrayList<>();

    /**
     * Derivated from User, who has a "game" column with the game where he/she is
     * playing in.
     * 
     * @author andrsdt
     * @return game where the player is playing in.
     */
    @Transient
    public Game getGame() {
        return user.getLobby().getGame();
    }

    @Transient
    public CharacterTypeEnum getCharacterTypeEnum() {
        return characterType.getCharacterTypeEnum();
    }
}
