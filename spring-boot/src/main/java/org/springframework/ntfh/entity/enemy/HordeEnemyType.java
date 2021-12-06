package org.springframework.ntfh.entity.enemy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
@Entity
@Table(name = "horde_enemy_types")
public class HordeEnemyType {
    @Id
    @Enumerated(EnumType.STRING)
    private HordeEnemyTypeEnum hordeEnemyTypeEnum;

    @Enumerated(EnumType.STRING)
    private HordeModifierTypeEnum hordeEnemyModifierTypeEnum;

    @NotNull
    private Integer endurance;
}
