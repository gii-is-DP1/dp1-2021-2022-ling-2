package org.springframework.ntfh.entity.turn.concretestates;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.command.ReceiveDamageCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;

@State
public class MarketState implements TurnState {

    @Autowired
    UserService userService;

    @Autowired
    TurnService turnService;

    @Autowired
    MarketCardIngameService marketCardIngameService;

    @Autowired
    AbilityCardIngameService abilityCardIngameService;

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.PLAYER_STATE;
    }

    @Override
    public void postState(Game game) {
        // After the market state, the player will receive damage from the horde
        // and then a new turn will be created
        Player currentPlayer = game.getCurrentTurn().getPlayer();
        game.getEnemiesFighting().forEach(enemy -> {
            new ReceiveDamageCommand(enemy, currentPlayer).execute();
        });

        turnService.createNextTurn(game);

    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        throw new IllegalStateException("You can't play cards now");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {

        // TODO throw exception if not player's turn?

        String username = TokenUtils.usernameFromToken(token);
        User user = userService.findUser(username);
        Player player = user.getPlayer();

        MarketCardIngame marketCardIngame = marketCardIngameService.findById(marketCardIngameId);

        Set<ProficiencyTypeEnum> marketCardProficiencies = marketCardIngame.getMarketCard().getProficiencies();
        Set<ProficiencyTypeEnum> playerProficiencies = player.getCharacter().getProficiencies().stream()
                .map(Proficiency::getProficiencyTypeEnum).collect(Collectors.toSet());

        playerProficiencies.retainAll(marketCardProficiencies); // Intersection of both sets

        if (!marketCardProficiencies.isEmpty() && playerProficiencies.isEmpty()) {
            throw new IllegalArgumentException(
                    "This card is not available for the " + player.getCharacterTypeEnum().toString().toLowerCase());
        }

        Integer price = marketCardIngame.getMarketCard().getPrice();
        if (player.getGold() < price) {
            throw new IllegalArgumentException("You do not have enough gold to buy this card");
        }

        player.setGold(player.getGold() - price);
        AbilityCardIngame marketCardAsAbilityCard =
                abilityCardIngameService.createFromMarketCard(marketCardIngame.getMarketCard(), player);
        player.getHand().add(marketCardAsAbilityCard);

        // Remove the card from the For Sale pile and it from the database
        Game game = player.getGame();
        game.getMarketCardsForSale().remove(marketCardIngame);
        marketCardIngameService.delete(marketCardIngame);
    }

}
