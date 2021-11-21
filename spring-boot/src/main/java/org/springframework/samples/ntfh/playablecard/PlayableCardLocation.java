package org.springframework.samples.ntfh.playablecard;

public enum PlayableCardLocation {
    /** The card is player's hand */
    HAND,

    /** The card is player's pile */
    PILE,

    /** The card is in a player's discard pile */
    DISCARD_PILE,

    /** The card is exiled and cannot be used anymore */
    EXILED,

    MARKET

}
