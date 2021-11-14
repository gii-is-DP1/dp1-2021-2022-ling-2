package org.springframework.samples.ntfh.enemy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "horde_enemies")
public class HordeEnemy extends Enemy {
    @NotNull
    private Integer glory;

    private Integer extraGlory;

    private Integer gold;

    private HordeModifier modifier;
}
