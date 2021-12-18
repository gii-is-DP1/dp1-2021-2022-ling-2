package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Service;

@Service
public class AbilityCardIngameService {

    @Autowired
    private AbilityCardIngameRepository abilityCardIngameRepository;

    @Autowired
    private AbilityCardService abilityCardService;

    /**
     * Given a game, instantiate the abilityCardIngame entities for each of the
     * players
     * 
     * @author andrsdt
     * @param game that the ability cards will be initialized for
     */
    @Transactional
    public void initializeFromGame(Game game) {
        game.getPlayers().forEach(this::populateWithInitialCards);
    }

    /**
     * Given a player, fill his/her abilityPile with all the cards of his/her
     * character
     * 
     * @param player
     * @return
     */
    private void populateWithInitialCards(Player player) {
        CharacterTypeEnum character = player.getCharacterTypeEnum();
        List<AbilityCard> allAbilityCardsForCharacter = StreamSupport
                .stream(abilityCardService.findByCharacterTypeEnum(character).spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allAbilityCardsForCharacter);

        List<AbilityCardIngame> playerAbilityCards = allAbilityCardsForCharacter
                .stream()
                .map(abilityCard -> createFromAbilityCard(abilityCard, player))
                .collect(Collectors.toList());

        player.setCards(playerAbilityCards);

        refillHandWithCards(player);

    }

    /**
     * Given a player, fill his/her hand with cards from their abilityPile until
     * they have 4 or they run out of cards
     * 
     * @param player
     */
    private void refillHandWithCards(Player player) {

        List<AbilityCardIngame> playerAbilityPile = player.getAbilityPile();
        List<AbilityCardIngame> playerHand = player.getHand();

        while (!playerAbilityPile.isEmpty() && playerHand.size() < 4) {
            AbilityCardIngame lastAbilityCardInPile = playerAbilityPile.get(0);
            playerAbilityPile.remove(lastAbilityCardInPile);
            lastAbilityCardInPile.setLocation(AbilityCardLocationEnum.HAND);
            playerHand.add(lastAbilityCardInPile);
        }

        // Join both lists back together and save them in the DB
        List<AbilityCardIngame> marketCardsInPileAndForSale = Stream.of(playerAbilityPile, playerHand)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        player.setCards(marketCardsInPileAndForSale);
    }

    /**
     * Given a generic ability card and a player, create the specific Ingame entity
     * for this player
     * 
     * @param abilityCard
     * @param player
     * @return
     */
    @Transactional
    private AbilityCardIngame createFromAbilityCard(AbilityCard abilityCard, Player player) {
        AbilityCardIngame abilityCardIngame = new AbilityCardIngame();
        abilityCardIngame.setAbilityCard(abilityCard);
        abilityCardIngame.setLocation(AbilityCardLocationEnum.ABILITY_PILE);
        abilityCardIngameRepository.save(abilityCardIngame);
        return abilityCardIngame;
    }
}