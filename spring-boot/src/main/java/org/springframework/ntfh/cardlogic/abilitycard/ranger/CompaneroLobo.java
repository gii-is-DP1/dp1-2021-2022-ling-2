package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.Command;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngame;
import org.springframework.ntfh.entity.player.Player;

public class CompaneroLobo implements Command {
    Integer gameId;
    Player playerFrom;
    HordeEnemyIngame enemyTo;

    // TODO commands declaration

    // TODO autowired services declaration

    // TODO constructor for all the previous attributes

    @Override
    public void execute() {
        System.out.println(this.toString());
    }
}
