package org.springframework.samples.ntfh.playablecard;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.character.CharacterTypeEnum;
import org.springframework.samples.ntfh.entity.model.BaseEntity;

import lombok.Getter;

// TODO we have to change the implementation of this class or remove it
@Getter
@Entity
@Table(name = "playable_cards")
public class PlayableCard extends BaseEntity {

    // TODO esta propiedad no debería estar aquí. Tenemos que hacer una entidad
    // nueva que sea de instancias de cartas ingame y ahí será donde la carta tenga
    // una posición, un poseedor... Aquí solo se guardan propiedades generales de la
    // carta, comunes a todas las partidas, y a partir de aquí se crearán las
    // instancias específicas de las cartas para cada partida

    @Enumerated(EnumType.STRING)
    private PlayableCardLocation location;

    // @Enumerated(EnumType.STRING)
    // private AbilityCard cardType; // Enum que englobe las AbilityCard de todos
    // los personajes

    // TODO refactorizar
    public String switchCharacter(String character) {
        if (character.matches(CharacterTypeEnum.RANGER.toString()))
            return RangerCard.values().toString();
        else if (character.matches(CharacterTypeEnum.ROGUE.toString()))
            return RogueCard.values().toString();
        else if (character.matches(CharacterTypeEnum.WARRIOR.toString()))
            return WarriorCard.values().toString();
        else if (character.matches(CharacterTypeEnum.WIZARD.toString()))
            return WizardCard.values().toString();
        else
            return null;
    }

    public String getFrontImage() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBackImage() {
        // TODO Auto-generated method stub
        return null;
    }
}
