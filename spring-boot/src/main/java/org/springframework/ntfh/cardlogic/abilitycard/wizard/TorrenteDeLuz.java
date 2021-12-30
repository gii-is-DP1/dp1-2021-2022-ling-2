package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class TorrenteDeLuz {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, Game game, AbilityCardIngame cardPlayed){
        new DealDamageCommand(2, targetedEnemy).execute();
        new GetGloryCommand(1, playerFrom).execute();
        List<Player> targets = game.getPlayers();
        for(Player playerTarget:targets){
            new RecoverCommand(2, playerTarget).execute();
        }
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
    
}
