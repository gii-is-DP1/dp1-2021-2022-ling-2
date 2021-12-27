package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.ntfh.entity.model.BaseEntity;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
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
}
