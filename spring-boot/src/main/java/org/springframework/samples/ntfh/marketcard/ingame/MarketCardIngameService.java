package org.springframework.samples.ntfh.marketcard.ingame;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.marketcard.MarketCard;
import org.springframework.samples.ntfh.marketcard.MarketCardService;
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
     * @param game that the market cards will be instantiated for
     */
    @Transactional
    public void createFromGame(Game game) {
        // Fetch all market cards from the database
        List<MarketCard> allMarketCards = StreamSupport.stream(marketCardService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allMarketCards);

        // Create a new market card ingame instance for each market card in the database
        // (since they are all going to be used in the game)
        Integer i = 0;
        for (MarketCard marketCard : allMarketCards) {
            MarketCardIngame marketCardIngame = new MarketCardIngame();
            marketCardIngame.setGame(game);
            marketCardIngame.setMarketCard(marketCard);

            // The first 5 cards will be shown as available to buy initially
            MarketCardLocation location = (i < 5) ? MarketCardLocation.MARKET_FOR_SALE : MarketCardLocation.MARKET_PILE;
            i++;
            marketCardIngame.setLocation(location);

            this.save(marketCardIngame);
        }
    }
}
