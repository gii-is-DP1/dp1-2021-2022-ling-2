package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

import java.util.List;

public class TorrenteDeLuz {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, Game game){
        new DealDamageCommand(2, targetedEnemy).execute();
        new GetGloryCommand(1, playerFrom);
        List<Player> players = game.getPlayers();
        Integer i = 0;
        for(i=0; i<players.size(); i++){
            new RecoverCommand(2, players.get(i)).execute();
        }
    }
    
}
