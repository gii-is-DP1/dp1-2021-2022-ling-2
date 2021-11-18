package org.springframework.samples.ntfh.playableCard;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.ntfh.character.CharacterType;
import org.springframework.samples.ntfh.interfaces.AbilityCard;
import org.springframework.samples.ntfh.interfaces.Location;
import org.springframework.samples.ntfh.interfaces.PhysicalCard;
import org.springframework.samples.ntfh.interfaces.PlayableCardType;
import org.springframework.samples.ntfh.marketCard.MarketCardEnum;
import org.springframework.samples.ntfh.model.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "playable_cards")
public class PlayableCard extends BaseEntity implements PhysicalCard {

    // TODO esta propiedad no debería estar aquí. Tenemos que hacer una entidad nueva que sea de instancias de
    // cartas ingame y ahí será donde la carta tenga una posición, un poseedor... Aquí solo se guardan propiedades
    // generales de la carta, comunes a todas las partidas, y a partir de aquí se crearán las instancias específicas
    // de las cartas para cada partida
    @Enumerated(EnumType.STRING)
    private PlayableCardLocation location;

 //   @Enumerated(EnumType.STRING)
 //   private AbilityCard cardType; // Enum que englobe las AbilityCard de todos los personajes
    
    // TODO refactorizar
    public String switchCharacter(String character) {
        if(character.matches(CharacterType.RANGER.toString())) return RangerCard.values().toString();
        else if (character.matches(CharacterType.ROGUE.toString())) return RogueCard.values().toString();
        else if (character.matches(CharacterType.WARRIOR.toString())) return WarriorCard.values().toString();
        else if (character.matches(CharacterType.WIZARD.toString())) return WizardCard.values().toString();
        else return null;
    }

    @Override
    public String getFrontImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBackImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }
}