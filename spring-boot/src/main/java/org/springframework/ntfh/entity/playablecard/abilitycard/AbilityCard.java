package org.springframework.ntfh.entity.playablecard.abilitycard;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ability_cards")
public class AbilityCard extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private AbilityCardTypeEnum abilityCardTypeEnum;

    @Enumerated(EnumType.STRING)
    private CharacterTypeEnum characterTypeEnum;

    @NotNull
    private Integer baseDamage;

}
