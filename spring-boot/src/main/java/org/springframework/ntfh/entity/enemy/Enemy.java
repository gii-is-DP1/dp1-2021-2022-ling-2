package org.springframework.ntfh.entity.enemy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;

/**
 * Entity that contains base information about an enemy. Not concrete to any
 * game. This will be used to instantiate the initial enemies ingame.
 * 
 * @author andrsdt
 */
@Getter
@Entity
@Table(name = "enemies")
public class Enemy extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private EnemyType enemyType;

    @Enumerated(EnumType.STRING)
    private EnemyCategoryType enemyCategoryType;

    @Enumerated(EnumType.STRING)
    private EnemyModifierType enemyModifierType;

    @NotNull
    private Integer endurance;

    @NotNull
    private Integer gold;

    @NotNull
    private Integer baseGlory;

    @NotNull
    private Integer extraGlory;
}
