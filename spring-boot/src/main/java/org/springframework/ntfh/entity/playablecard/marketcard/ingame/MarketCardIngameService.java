package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
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
    private GameService gameService;

    @Transactional
    public Iterable<MarketCardIngame> findMarketCardsByGameId(Integer gameId) {
        return gameService.findGameById(gameId).getMarketCards();
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
     * Create entities of each market card in the game passed as a parameter and
     * persist them in the database.
     * 
     * @author andrsdt
     * @param game that the market cards will be initialized for
     */

    /**
     * Keep taking market cards from the pile and adding them to the market area
     * while there are less than 5
     * 
     * @author @andrsdt
     */
    private void refillMarketWithCards(Game game) {
        // Get a list of
        List<MarketCardIngame> marketCardsIngame = game.getMarketCards();
        List<MarketCardIngame> marketCardsInPile = marketCardsIngame.stream()
                .filter(mc -> mc.getLocation() == MarketCardLocation.MARKET_PILE).collect(Collectors.toList());
        List<MarketCardIngame> marketCardsForSale = marketCardsIngame.stream()
                .filter(mc -> mc.getLocation() == MarketCardLocation.MARKET_FOR_SALE).collect(Collectors.toList());

        while (!marketCardsInPile.isEmpty() && marketCardsForSale.size() < 5) {
            MarketCardIngame lastMarketCardInPile = marketCardsInPile.get(0);
            marketCardsInPile.remove(lastMarketCardInPile);
            lastMarketCardInPile.setLocation(MarketCardLocation.MARKET_FOR_SALE);
            marketCardsForSale.add(lastMarketCardInPile);
        }

        // Join both lists back together and save them in the DB
        List<MarketCardIngame> marketCardsInPileAndForSale = Stream.of(marketCardsInPile, marketCardsForSale)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        game.setMarketCards(marketCardsInPileAndForSale);

    }

    @Transactional
    public void initializeFromGame(Game game) {
        // Fetch all market cards from the database
        List<MarketCard> allMarketCards = StreamSupport.stream(marketCardService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allMarketCards);

        List<MarketCardIngame> gameMarketCards = allMarketCards.stream() // From the shuffled list of possible enemies
                .map(marketCard -> createFromMarketCard(marketCard, game)) // And create the DB row of each one
                .collect(Collectors.toList());

        game.setMarketCards(gameMarketCards);

        refillMarketWithCards(game);
    }

    @Transactional
    private MarketCardIngame createFromMarketCard(MarketCard marketCard, Game game) {
        MarketCardIngame marketCardIngame = new MarketCardIngame();
        // marketCardIngame.setGame(game);
        marketCardIngame.setMarketCard(marketCard);
        marketCardIngame.setLocation(MarketCardLocation.MARKET_PILE);
        this.save(marketCardIngame);
        return marketCardIngame;
    }
}
