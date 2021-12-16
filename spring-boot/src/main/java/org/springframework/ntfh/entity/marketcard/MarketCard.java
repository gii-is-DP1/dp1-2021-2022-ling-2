package org.springframework.ntfh.entity.marketcard;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.proficiency.Proficiency;

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

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "marketcards_proficiencies", joinColumns = @JoinColumn(name = "market_card_id"), inverseJoinColumns = @JoinColumn(name = "proficiency_id"))
    @JsonIgnore
    private Set<Proficiency> proficiencies; // if null, usable by anyone

    @Transient
    public String getFrontImage() {
        return "/cards/items/" + getMarketCardTypeEnum().toString().toLowerCase() + ".png";
    }

    @Transient
    public String getBackImage() {
        return "/cards/back_standard.png";
    }

}
