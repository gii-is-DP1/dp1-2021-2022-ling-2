package org.springframework.samples.ntfh.playableCard;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.interfaces.AbilityCard;
import org.springframework.samples.ntfh.interfaces.Location;
import org.springframework.samples.ntfh.interfaces.PhysicalCard;
import org.springframework.samples.ntfh.interfaces.PlayableCardType;
import org.springframework.samples.ntfh.model.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "playable_cards")
public class PlayableCard extends BaseEntity implements PhysicalCard {

    @Enumerated(EnumType.STRING)
    private PlayableCardLocation location;

 //   @Enumerated(EnumType.STRING)
 //   private AbilityCard cardType; // Enum que englobe las AbilityCard de todos los personajes

    @Override
    public String getFrontImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBackImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }
}
