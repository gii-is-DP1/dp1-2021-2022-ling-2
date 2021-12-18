package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import org.springframework.ntfh.interfaces.Location;

public enum MarketCardLocation implements Location {
    // TODO also could be attacking enemy 1, attacking enemy 2, attacking enemy 3,
    // attacking warlord. This should be common to all attack cards so maybe we
    // could create an enum for this and extend from these
    MARKET_PILE, MARKET_FOR_SALE, RANGER_HAND, ROGUE_HAND, WARRIOR_HAND, WIZARD_HAND, EXILE
}
