package org.springframework.ntfh.entity.enemy.hordeenemy.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.enemy.EnemyLocation;
import org.springframework.ntfh.entity.enemy.hordeenemy.HordeEnemy;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "horde_enemies_ingame")
public class HordeEnemyIngame extends BaseEntity {
    // TODO extends IngameEntity?
    @ManyToOne(optional = false)
    @JoinColumn(name = "horde_enemy_id")
    private HordeEnemy hordeEnemy;

    @NotNull
    private Integer currentEndurance;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    @Enumerated(EnumType.STRING)
    private EnemyLocation hordeEnemyLocation;

    @ManyToOne
    @JsonIgnore
    private Game game;
}
