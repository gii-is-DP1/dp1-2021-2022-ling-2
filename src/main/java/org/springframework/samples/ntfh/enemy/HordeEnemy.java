package org.springframework.samples.ntfh.enemy;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

// Entity that contains base information about an enemy.
// Not concrete to any game. This will be used to instantiate
// the initial enemies ingame.
@Getter
@Entity
@Table(name = "horde_enemies")
public class HordeEnemy extends EnemyEntity {
    // FK
    @OneToMany // TODO mappedBy?
    @JsonIgnore
    private HordeEnemyType hordeEnemyType;

    @NotNull
    private Integer gold;

    @NotNull
    private Integer extraGlory;

    @Transient
    private String backImage;

    @Transient
    private String frontImage;

    /**
     * Derived. Return the route to the image of an enemy. Can be obtained by
     * knowing the HordeEnemyType and, in case of the warrior, also the loot behind
     * the card
     * 
     * @author andrsdt
     * @return route to the card image
     */
    public String getFrontImage() {
        // WarlordType.GURDRUG -> gurdrug
        HordeEnemyTypeEnum type = hordeEnemyType.getHordeEnemyTypeEnum();
        String hordeEnemyName = type.toString().toLowerCase();

        // The warrior with extraGlory and gold has a different image
        if ((type.equals(HordeEnemyTypeEnum.WARRIOR)) && (extraGlory != null || gold != null))
            hordeEnemyName = "warrior_with_loot";

        return "/cards/enemies/horde_enemies/fronts/" + hordeEnemyName + ".png";
    }

    /**
     * Derived. Return the route to the standard back image of a HordeEnemy.
     * 
     * @author andrsdt
     * @return String route to the card's back image
     */
    public String getBackImage() {
        return String.format("/cards/enemies/horde_enemies/backs/%dgold_%dglory.png", gold, extraGlory);
    }
}
