package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;
import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ability_cards_ingame")
public class AbilityCardIngame extends BaseEntity {

    @ManyToOne
    @JsonIgnore
    private Player player;

    @ManyToOne(optional = false)
    private AbilityCard abilityCard;

    @Transient
    @JsonIgnore
    public AbilityCardTypeEnum getAbilityCardTypeEnum() {
        return abilityCard.getAbilityCardTypeEnum();
    }

    @NotNull
    public Integer baseDamage;
}
