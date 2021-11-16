package org.springframework.samples.ntfh.marketCard;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.character.CharacterType;
import org.springframework.samples.ntfh.interfaces.Location;
import org.springframework.samples.ntfh.interfaces.PhysicalCard;
import org.springframework.samples.ntfh.model.BaseEntity;
import org.springframework.samples.ntfh.playableCard.PlayableCardLocation;

import lombok.Getter;

@Getter
@Entity
@Table(name = "market_cards")
public class MarketCard extends BaseEntity implements PhysicalCard {

    @Enumerated(EnumType.STRING)
    private PlayableCardLocation location;

    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    private MarketCardEnum name;

    @Column(name = "usableBy")
    private String usableBy;

//    private List<CharacterType> usableBy; sql no puede reflejar listas, asi que tendra que guardarse como String


    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

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


    
}
