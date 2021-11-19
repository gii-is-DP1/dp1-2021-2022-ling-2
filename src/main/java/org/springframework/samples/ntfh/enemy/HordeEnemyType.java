package org.springframework.samples.ntfh.enemy;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Table(name = "horde_enemy_types")
public class HordeEnemyType {
    @Enumerated(EnumType.STRING)
    private HordeEnemyTypeEnum hordeEnemyTypeEnum;

    @Enumerated(EnumType.STRING)
    private HordeModifierTypeEnum hordeEnemyModifierTypeEnum;

    @Enumerated(EnumType.STRING)
    private Integer endurance;

}
