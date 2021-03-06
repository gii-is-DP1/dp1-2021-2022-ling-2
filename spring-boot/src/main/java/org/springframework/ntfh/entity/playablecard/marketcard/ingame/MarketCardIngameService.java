package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

/**
 * @author andrsdt
 */
@Service
public class MarketCardIngameService {

    @Autowired
    private MarketCardIngameRepository marketCardIngameRepository;

    @Autowired
    private MarketCardService marketCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private TurnService turnService;

    public MarketCardIngame findById(Integer id) throws DataAccessException {
        Optional<MarketCardIngame> enemyIngame = marketCardIngameRepository.findById(id);
        if (!enemyIngame.isPresent())
            throw new DataAccessException("MarketCardIngame with id " + id + " was not found") {};
        return enemyIngame.get();
    }

    @Transactional
    public void save(MarketCardIngame marketCardIngame) {
        this.marketCardIngameRepository.save(marketCardIngame);
    }

    @Transactional
    public void delete(MarketCardIngame marketCardIngame) {
        this.marketCardIngameRepository.delete(marketCardIngame);
    }

    /**
     * Keep taking market cards from the pile and adding them to the market area while there are less than 5
     * 
     * @author andrsdt
     */
    @Transactional
    public void refillMarketWithCards(Game game) {
        // Get a list of
        List<MarketCardIngame> marketCardsInPile = game.getMarketCardsInPile();
        List<MarketCardIngame> marketCardsForSale = game.getMarketCardsForSale();

        while (!marketCardsInPile.isEmpty() && marketCardsForSale.size() < 5) {
            MarketCardIngame lastMarketCardInPile = marketCardsInPile.get(0);
            marketCardsInPile.remove(lastMarketCardInPile);
            marketCardsForSale.add(lastMarketCardInPile);
        }
    }

    /**
     * Create entities of each market card in the game passed as a parameter and persist them in the database.
     * 
     * @author andrsdt
     * @param game that the market cards will be initialized for
     */
    @Transactional
    public void initializeFromGame(Game game) {
        // Fetch all market cards from the database
        List<MarketCard> allMarketCards =
                StreamSupport.stream(marketCardService.findAll().spliterator(), false).collect(Collectors.toList());

        Collections.shuffle(allMarketCards);

        List<MarketCardIngame> gameMarketCards = allMarketCards.stream() // From the shuffled list of possible enemies
                .map(marketCard -> createFromMarketCard(marketCard, game)) // And create the DB row of each one
                .collect(Collectors.toList());

        game.getMarketCardsInPile().addAll(gameMarketCards);

        refillMarketWithCards(game);
    }

    @Transactional
    public MarketCardIngame createFromMarketCard(MarketCard marketCard, Game game) {
        MarketCardIngame marketCardIngame = new MarketCardIngame();
        marketCardIngame.setMarketCard(marketCard);
        this.save(marketCardIngame);
        return marketCardIngame;
    }


    /**
     * Executed when a player tries to buy a market card
     * 
     * @param marketCardIngameId
     */
    @Transactional
    public Game buyMarketCard(Integer marketCardIngameId, String token) {
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findByUsername(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.buyMarketCard(marketCardIngameId, token);
        return player.getGame();
    }
}
