package org.springframework.ntfh.entity.playablecard.marketcard;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;

import lombok.Getter;

@Getter
@Entity
@Table(name = "market_cards")
public class MarketCard extends BaseEntity {

    @NotNull
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_card_type_enum")
    private MarketCardTypeEnum marketCardTypeEnum;

    // Set of proficiencies needed to use the card. If null, usable by anyone
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = ProficiencyTypeEnum.class)
    @CollectionTable(name = "marketcards_proficiencies", joinColumns = @JoinColumn(name = "market_card_id"))
    @Column(name = "proficiency_type_enum")
    private Set<ProficiencyTypeEnum> proficiencies;

    @Transient
    public String getFrontImage() {
        return "/cards/items/" + getMarketCardTypeEnum().toString().toLowerCase() + ".png";
    }

    @Transient
    public String getBackImage() {
        return "/cards/back_standard.png";
    }

}
