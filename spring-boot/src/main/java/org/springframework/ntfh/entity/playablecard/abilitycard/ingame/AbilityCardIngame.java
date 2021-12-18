package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ability_cards_ingame")
public class AbilityCardIngame extends BaseEntity {

    @ManyToOne(optional = false)
    private AbilityCard abilityCard;

    @Enumerated(EnumType.STRING)
    private AbilityCardLocationEnum location;
}
