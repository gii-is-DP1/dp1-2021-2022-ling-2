package org.springframework.ntfh.entity.turn.concretestates;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Component;

@Component
public class MarketState implements TurnState {

    @Autowired
    UserService userService;

    @Autowired
    MarketCardIngameService marketCardIngameService;

    @Autowired
    AbilityCardIngameService abilityCardIngameService;

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.ENEMY_STATE;
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId) {
        throw new IllegalStateException("You can't play a card in the enemy attack stage");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {

        // TODO Throw exception if it's not the player's turn
        // TODO Throw exception if not in the market stage

        String username = TokenUtils.usernameFromToken(token);
        User user = userService.findUser(username);
        Player player = user.getPlayer();

        MarketCardIngame marketCardIngame = marketCardIngameService.findById(marketCardIngameId);

        Set<ProficiencyTypeEnum> marketCardProficiencies = marketCardIngame.getMarketCard().getProficiencies();
        Set<ProficiencyTypeEnum> playerProficiencies = player.getCharacterType().getProficiencies().stream()
                .map(Proficiency::getProficiencyTypeEnum).collect(Collectors.toSet());

        playerProficiencies.retainAll(marketCardProficiencies); // Intersection of both sets

        if (!marketCardProficiencies.isEmpty() && playerProficiencies.isEmpty()) {
            throw new IllegalArgumentException(
                    "This card is not available for the " + player.getCharacterTypeEnum().toString().toLowerCase());
        }

        if (player.getHand().size() >= 4) {
            throw new IllegalArgumentException("You can't have more than 4 cards in your hand");
        }

        Integer price = marketCardIngame.getMarketCard().getPrice();
        if (player.getGold() < price) {
            throw new IllegalArgumentException("You do not have enough gold to buy this card");
        }

        player.setGold(player.getGold() - price);
        AbilityCardIngame marketCardAsAbilityCard = abilityCardIngameService.createFromMarketCard(
                marketCardIngame.getMarketCard(),
                player);
        player.getHand().add(marketCardAsAbilityCard);

        // Remove the card from the For Sale pile and it from the database
        Game game = player.getGame();
        game.getMarketCardsForSale().remove(marketCardIngame);
        marketCardIngameService.delete(marketCardIngame);
    }

}
