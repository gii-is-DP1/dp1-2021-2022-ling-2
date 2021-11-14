package org.springframework.samples.ntfh.enemy;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.enumerates.HordeEnemyType;
import org.springframework.samples.ntfh.enumerates.HordeModifier;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

// No getters needed? These entities will stay the same
@Getter
@Entity
@Table(name = "horde_enemies")
public class HordeEnemy extends BaseEntity implements Enemy {
    @Column(name = "extra_glory", columnDefinition = "integer default 0")
    private Integer extraGlory;

    @Column(columnDefinition = "integer default 0")
    private Integer gold;

    @Enumerated(EnumType.STRING)
    private HordeEnemyType type;

    /**
     * Return the HordeModifier of an enemy, or null if it doesn't have any. Derived
     * from HordeEnemyType.
     * 
     * @author andrsdt
     * @return HordeModifier enum value of the HordeEnemy
     */
    public HordeModifier getHordeModifier() {
        // Derived property,
        HordeModifier modifier = null;
        if (type == HordeEnemyType.SHAMAN || type == HordeEnemyType.MAGE) {
            modifier = HordeModifier.MAGIC_ATTACKER;
        } else if (type == HordeEnemyType.REGEN) {
            modifier = HordeModifier.HEALING_CAPABILITIES;
        }
        return modifier;
    }

    /**
     * Return the endurance of an enemy computed by knowing its HordeEnemyType.
     * 
     * @author andrsdt
     * @return Integer value of the HordeEnemy's endurance
     */
    @Override
    public Integer getEndurance() {
        Map<HordeEnemyType, Integer> enemyEndurance = Map.of(HordeEnemyType.SLINGER, 2, HordeEnemyType.REGEN, 3,
                HordeEnemyType.SHAMAN, 3, HordeEnemyType.WARRIOR, 4, HordeEnemyType.MAGE, 5, HordeEnemyType.BERSERKER,
                6);

        return enemyEndurance.get(type);
    }

    /**
     * Return the route to the image of an enemy. Can be obtained by knowing the
     * HordeEnemyType and, in case of the warrior, also the loot behind the card
     * 
     * @author andrsdt
     * @return route to the card image
     */
    @Override
    public String getFrontImage() {
        // Derived property. Can be obtained by knowing the EnemyType, and in some
        // specific cases the extraGlory and gold

        // WarlordType.GURDRUG -> gurdrug
        String hordeEnemyName = type.toString().toLowerCase();

        // The warrior with extraGlory and gold has a different image
        if ((type == HordeEnemyType.WARRIOR) && (extraGlory != null || gold != null))
            hordeEnemyName = "warrior_with_loot";

        return "/cards/enemies/horde_enemies/fronts/" + hordeEnemyName + ".png";
    }

    /**
     * Return the route to the standard back image of a HordeEnemy.
     * 
     * @author andrsdt
     * @return String route to the card's back image
     */
    @Override
    public String getBackImage() {
        return String.format("/cards/enemies/horde_enemies/backs/%dgold_%dglory.png", gold, extraGlory);
    }

}
