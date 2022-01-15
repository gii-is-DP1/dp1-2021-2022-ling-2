package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.command.GiveWoundCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Service;

@Service
public class AbilityCardIngameService {

    @Autowired
    private AbilityCardIngameRepository abilityCardIngameRepository;

    @Autowired
    private AbilityCardService abilityCardService;

    public AbilityCardIngame findById(int id) {
        return abilityCardIngameRepository.findById(id)
                .orElseThrow(() -> new DataAccessException("Ability card not found") {
                });
    }

    public void delete(AbilityCardIngame abilityCardIngame) {
        abilityCardIngameRepository.delete(abilityCardIngame);
    }

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

        player.setAbilityPile(playerAbilityCards);

        refillHandWithCards(player);

    }

    /**
     * Given a player, fill his/her hand with cards from their abilityPile until
     * they have 4 or they run out of cards
     * 
     * @param player
     */
    public void refillHandWithCards(Player player) {
        if (player.isDead())
            return;

        List<AbilityCardIngame> playerAbilityPile = player.getAbilityPile();
        List<AbilityCardIngame> playerHand = player.getHand();

        while (!playerAbilityPile.isEmpty() && playerHand.size() < 4) {
            AbilityCardIngame lastAbilityCardInPile = playerAbilityPile.get(0);
            playerAbilityPile.remove(lastAbilityCardInPile);
            if (playerAbilityPile.isEmpty())
                new GiveWoundCommand(player).execute();

            playerHand.add(lastAbilityCardInPile);
        }
        player.setAbilityPile(playerAbilityPile);
        player.setHand(playerHand);
    }

    /**
     * Given a generic ability card and a player, create the specific Ingame entity
     * for this player
     * 
     * @author andrsdt
     * @param abilityCard
     * @param player
     * @return
     */
    @Transactional
    public AbilityCardIngame createFromAbilityCard(AbilityCard abilityCard, Player player) {
        AbilityCardIngame abilityCardIngame = new AbilityCardIngame();
        abilityCardIngame.setPlayer(player);
        abilityCardIngame.setAbilityCard(abilityCard);
        abilityCardIngame.setBaseDamage(abilityCard.getBaseDamage());
        abilityCardIngameRepository.save(abilityCardIngame);
        return abilityCardIngame;
    }

    /**
     * Given a market card, transform it into an instance of AbilityCardIngame
     * 
     * @author andrsdt
     * @param abilityCard
     * @param player
     * @return
     */
    @Transactional
    public AbilityCardIngame createFromMarketCard(MarketCard marketCard, Player player) {
        AbilityCardTypeEnum abilityCardTypeEnum = AbilityCardTypeEnum
                .valueOf(marketCard.getMarketCardTypeEnum().toString());
        AbilityCard abilityCard = abilityCardService.findByAbilityCardTypeEnum(abilityCardTypeEnum);

        return createFromAbilityCard(abilityCard, player);
    }
}
