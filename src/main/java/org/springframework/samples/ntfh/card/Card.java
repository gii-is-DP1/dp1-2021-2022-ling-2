package org.springframework.samples.ntfh.card;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.ntfh.enumerates.CardType;
import org.springframework.samples.ntfh.enumerates.Location;
import org.springframework.samples.ntfh.model.NamedEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "cards")
public class Card extends NamedEntity{
    
    private Location location;
    private CardType cardType;

}
