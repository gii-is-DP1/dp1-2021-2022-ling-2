package org.springframework.samples.ntfh.character;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.samples.ntfh.entity.marketcard.MarketCard;
import org.springframework.samples.ntfh.entity.model.BaseEntity;

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

    // TODO lazy load
    @ManyToMany(mappedBy = "usableBy")
    @JsonIgnore
    private Set<MarketCard> availableMarketCards;
}
