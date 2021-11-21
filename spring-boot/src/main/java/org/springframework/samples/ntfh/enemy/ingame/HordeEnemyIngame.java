package org.springframework.samples.ntfh.enemy.ingame;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enemy.HordeEnemy;
import org.springframework.samples.ntfh.model.IngameEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "horde_enemies_ingame")
public class HordeEnemyIngame extends IngameEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "horde_enemy_id")
    private HordeEnemy hordeEnemy;

    @NotNull
    private Integer currentEndurance;

    // TODO check if this is overriding IngameEntity's location
    @NotNull
    private EnemyLocation hordeEnemyLocation;
}
