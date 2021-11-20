package org.springframework.samples.ntfh.marketcard;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.model.BaseEntity;

import lombok.Getter;

@Getter
@Entity
@Table(name = "market_cards")
public class MarketCard extends BaseEntity {

    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_card_type_enum")
    private MarketCardTypeEnum marketCardTypeEnum;

    // Inspiration from PetClinic for this ManyToMany big join (It creates an
    // association table)
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "marketcards_characters", joinColumns = @JoinColumn(name = "market_card_id"), inverseJoinColumns = @JoinColumn(name = "character_id"))
    @JsonIgnore
    private Set<Character> usableBy;

    @Transient
    private String backImage;

    @Transient
    private String frontImage;

    public String getFrontImage() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBackImage() {
        // TODO Auto-generated method stub
        return null;
    }

}
