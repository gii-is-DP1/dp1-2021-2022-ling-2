package org.springframework.ntfh.entity.enemy.warlord;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.EnemyEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "warlords")
public class Warlord extends EnemyEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private WarlordTypeEnum warlordTypeEnum;

    @NotNull
    private Integer endurance;

    /**
     * Derived. Return the route to the front image of a warlord. Can be obtained by
     * knowing the WarlordType
     * 
     * @author andrsdt
     * @return String route to the card's front image
     */
    @Transient
    public String getFrontImage() {
        return "/cards/enemies/warlords/" + warlordTypeEnum.toString().toLowerCase() + ".png";
    }

    /**
     * Derived. Return the route to the standard back image of a warlord.
     * 
     * @author andrsdt
     * @return String route to the card's back image
     */
    @Transient
    public String getBackImage() {
        return "/cards/enemies/warlords/backs/standard.png";
    }
}
