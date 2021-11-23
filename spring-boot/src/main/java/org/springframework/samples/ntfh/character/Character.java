package org.springframework.samples.ntfh.character;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.ntfh.marketcard.MarketCard;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "characters")
public class Character extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterTypeEnum characterTypeEnum;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CharacterGenderEnum characterGenderEnum;

    @ManyToMany(mappedBy = "usableBy")
    private Set<MarketCard> availableMarketCards;
}
