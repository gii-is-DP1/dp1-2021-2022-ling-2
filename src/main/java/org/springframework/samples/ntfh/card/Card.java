package org.springframework.samples.ntfh.card;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.enumerates.CardType;
import org.springframework.samples.ntfh.enumerates.Location;
import org.springframework.samples.ntfh.model.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cards")
public class Card extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    private Location location;
    @Enumerated(EnumType.STRING)
    private CardType cardType;

}
