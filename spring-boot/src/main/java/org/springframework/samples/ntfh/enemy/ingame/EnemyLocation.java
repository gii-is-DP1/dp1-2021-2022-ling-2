package org.springframework.samples.ntfh.enemy.ingame;

import org.springframework.samples.ntfh.interfaces.Location;

/**
 * Applicable for both HordeEnemyIngame and WardlordIngame entities in principle
 */
public enum EnemyLocation {
    /** The enemy is still waiting to fight */
    PILE,

    /** The enemy is currently fighting the player(s) */
    FIGHTING,

    /** The enemy has been slayed and can no longer appear */
    EXILED
}
