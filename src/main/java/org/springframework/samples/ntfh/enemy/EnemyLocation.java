package org.springframework.samples.ntfh.enemy;

import org.springframework.samples.ntfh.interfaces.Location;

public enum EnemyLocation implements Location {
    /** The enemy is still waiting to fight */
    PILE,

    /** The enemy is currently fighting the player(s) */
    FIGHTING,

    /** The enemy has been slayed and can no longer appear */
    EXILED
}
