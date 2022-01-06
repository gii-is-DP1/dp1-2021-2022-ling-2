package org.springframework.ntfh.entity.player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.Transient;
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
@Audited
@Table(name = "players")
public class Player extends BaseEntity {

    @OneToOne(mappedBy = "player")
    @JsonIgnoreProperties({ "password", "email", "enabled", "lobby", "game", "player", "character", "authorities" })
    private User user;

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

    // Should not change when user's character is changed. Once the
    // row is created in the databse, it stays the same
    @ManyToOne(optional = false)
    @NotAudited
    @JoinColumn(name = "character_id", referencedColumnName = "id")
    private Character characterType;

    @OneToMany
    @NotAudited
    private List<AbilityCardIngame> hand = new ArrayList<>();

    @OneToMany
    @NotAudited
    private List<AbilityCardIngame> abilityPile = new ArrayList<>();

    @OneToMany
    @NotAudited
    private List<AbilityCardIngame> discardPile = new ArrayList<>();

    // Make playerCardsInTurn transient?

    @Transient
    public CharacterTypeEnum getCharacterTypeEnum() {
        return characterType.getCharacterTypeEnum();
    }

    @Transient
    @JsonIgnore
    public Game getGame() {
        return user.getLobby().getGame();
    }

    @Transient
    public Boolean isDead() {
        return wounds.equals(characterType.getBaseHealth());
    }
}
