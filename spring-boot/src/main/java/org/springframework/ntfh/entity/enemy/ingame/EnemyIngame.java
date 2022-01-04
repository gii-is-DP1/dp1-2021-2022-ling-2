package org.springframework.ntfh.entity.enemy.ingame;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyCategoryType;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "enemies_ingame")
public class EnemyIngame extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "enemy_id") // TODO needed?
    private Enemy enemy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id") // TODO needed?
    @JsonIgnore
    private Game game;

    @NotNull
    private Integer currentEndurance;

    @NotNull
    private Boolean restrained;

    @Transient
    public Boolean isWarlord() {
        return enemy.getEnemyCategoryType() == EnemyCategoryType.WARLORD;
    }

    @Transient
    public Boolean isHorde() {
        return enemy.getEnemyCategoryType() == EnemyCategoryType.HORDE;
    }
}
