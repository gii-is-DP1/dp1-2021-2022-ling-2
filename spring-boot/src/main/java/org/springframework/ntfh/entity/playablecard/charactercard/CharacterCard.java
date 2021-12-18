package org.springframework.ntfh.entity.playablecard.charactercard;

import javax.persistence.Table;

import org.springframework.ntfh.entity.model.BaseEntity;

import lombok.Getter;

@Getter
@Table(name = "character_cards")
public class CharacterCard extends BaseEntity {
    // All the cards here? differentiate between each character's card with an enum?
    // We could also include marketCards there that way

    // Another idea: should this CharacterCard be extended by each one of the
    // player's custom entity "WizardCard", "RogueCard"... or should they all fit
    // here?
}
