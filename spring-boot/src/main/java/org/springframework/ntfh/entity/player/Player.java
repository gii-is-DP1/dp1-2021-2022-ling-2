package org.springframework.ntfh.entity.player;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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

    @ManyToOne
    @JsonIgnoreProperties({"password", "email", "enabled", "lobby", "game", "player", "character", "authorities"})
    private User user;

    @NotNull
    @PositiveOrZero
    private Integer glory;

    @NotNull
    @PositiveOrZero
    private Integer kills;

    @NotNull
    @PositiveOrZero
    private Integer gold;

    @NotNull
    @PositiveOrZero
    private Integer wounds;

    @NotNull
    @PositiveOrZero
    private Integer guard;

    // Should not change when user's character is changed. Once the
    // row is created in the databse, it stays the same
    @ManyToOne
    @JoinColumn(name = "character_id", referencedColumnName = "id") // TODO redundant referenceColumnName?
    private Character character;

    @OneToMany
    @NotAudited // TODO audit this?
    private List<AbilityCardIngame> hand = new ArrayList<>();

    @OneToMany
    @NotAudited // TODO audit this?
    private List<AbilityCardIngame> abilityPile = new ArrayList<>();

    @OneToMany
    @NotAudited // TODO audit this?
    private List<AbilityCardIngame> discardPile = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties({"startTime", "finishTime", "hasScenes", "spectatorsAllowed", "maxPlayers", "players",
            "leader", "winner", "turns", "enemiesInPile", "enemiesFighting", "marketCardsInPile", "marketCardsForSale",
            "comments", "currentTurn"})
    private Game game;

    @Transient
    public CharacterTypeEnum getCharacterTypeEnum() {
        return (character == null) ? null : character.getCharacterTypeEnum();
    }

    @Transient
    public Boolean isDead() {
        return (character != null) && wounds >= character.getBaseHealth();
    }

    @Transient
    public Integer getTurnOrder() {
        // TODO test if this works
        return (game == null || game.getPlayers().isEmpty()) ? null : game.getPlayers().indexOf(this);
    }
}
