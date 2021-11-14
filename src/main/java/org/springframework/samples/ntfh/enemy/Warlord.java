package org.springframework.samples.ntfh.enemy;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.enumerates.WarlordType;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "warlords")
public class Warlord extends BaseEntity implements Enemy {

    @NotNull
    @Enumerated(EnumType.STRING)
    private WarlordType type;

    /**
     * Derived. Return the endurance of a warlord. Can be obtained by knowing the
     * WarlordType
     * 
     * @author andrsdt
     * @return Integer value of the warlord's endurance
     */
    @Override
    public Integer getEndurance() {
        Map<WarlordType, Integer> warlordEndurance = Map.of(WarlordType.GURDRUG, 8, WarlordType.ROGHKILLER, 9,
                WarlordType.SHRIEKKNIFER, 10);

        return warlordEndurance.get(type);
    }

    /**
     * Derived. Return the route to the front image of a warlord. Can be obtained by
     * knowing the WarlordType
     * 
     * @author andrsdt
     * @return String route to the card's front image
     */
    @Override
    public String getFrontImage() {
        return "/cards/enemies/warlords/" + type.toString().toLowerCase() + ".png";
    }

    /**
     * Derived. Return the route to the standard back image of a warlord.
     * 
     * @author andrsdt
     * @return String route to the card's back image
     */
    @Override
    public String getBackImage() {
        return "/cards/enemies/warlords/backs/standard.png";
    }
}
