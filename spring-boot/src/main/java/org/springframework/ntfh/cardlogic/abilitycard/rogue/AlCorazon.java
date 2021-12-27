package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngame;
import org.springframework.ntfh.entity.player.Player;

public class AlCorazon {
    public void execute(Player playerFrom, HordeEnemyIngame targetedEnemy) {
        new DealDamageCommand(4, targetedEnemy).execute();
        new GoldOnKillCommand(1, targetedEnemy, playerFrom).execute();
//TODO        falta la implementación de que la condición del oro se ejecuta solo una vez por ronda
        new DiscardCommand(1, playerFrom).execute();
    }
}
