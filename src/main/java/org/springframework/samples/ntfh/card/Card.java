package org.springframework.samples.ntfh.card;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.interfaces.Location;
import org.springframework.samples.ntfh.interfaces.PhysicalCard;
import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "cards")
public class Card extends NamedEntity implements PhysicalCard {

    @Enumerated(EnumType.STRING)
    private CardLocation location;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

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
