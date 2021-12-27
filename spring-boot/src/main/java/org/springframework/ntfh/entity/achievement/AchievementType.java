package org.springframework.ntfh.entity.achievement;

/**
 * This approach will help us stablish a distinction between different
 * achievements when it comes to checking whether or not a user has fulfilled te
 * requirements to be given a certain achievement. This will be useful later for
 * implementing the business logic used to check every achievement, and to trace
 * it back to the Achievement object
 * 
 * @author andrsdt
 */
public enum AchievementType {
    CREATE_ACCOUNT, PLAY_1_GAME, WIN_5_GAMES

}
