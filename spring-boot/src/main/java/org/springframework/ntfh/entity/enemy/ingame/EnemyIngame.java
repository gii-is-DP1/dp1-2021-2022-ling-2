package org.springframework.ntfh.entity.enemy.ingame;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.Transient;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyCategoryType;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Audited
@Table(name = "enemies_ingame")
public class EnemyIngame extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "enemy_id") // TODO needed?
    @NotAudited
    private Enemy enemy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id") // TODO needed?
    @JsonIgnore
    private Game game;

    @NotNull
    private Integer currentEndurance;

    @NotNull
    private Boolean restrained;

    // TODO rename variable to something more meaningful (playerCardsOnMe sounds
    // like played ON the character, not on the enemy)
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = AbilityCardTypeEnum.class)
    @CollectionTable(name = "card_played_on_enemy_in_turn", joinColumns = @JoinColumn(name = "enemy_id"))
    @Column(name = "ability_card_type_enum")
    private Set<AbilityCardTypeEnum> playedCardsOnMeInTurn = new HashSet<>();

    @Transient
    @JsonIgnore
    public Boolean isWarlord() {
        return enemy.getEnemyCategoryType() == EnemyCategoryType.WARLORD;
    }

    @Transient
    @JsonIgnore
    public Boolean isHorde() {
        return enemy.getEnemyCategoryType() == EnemyCategoryType.HORDE;
    }

    @Transient
    @JsonIgnore
    public Boolean isDead() {
        return currentEndurance <= 0;
    }

    public void setCurrentEndurance(Integer currentEndurance) {
        this.currentEndurance = currentEndurance < 0 ? 0 : currentEndurance;
    }
}
