package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Daña a todos los enemigos. El resto de héroes sufre 1 de Daño.
 * 
 * @author Pablosancval
 */
@Component
public class BolaDeFuego {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        //el motivo de la creación de esta lista adicional vacía es evitar que una vez
        //un enemigo muere se elimine de la lista enemies fighting y por tanto el siguiente
        //enemigo de esa lista sea ignorado ya que el bucle busca el siguiente index, que pasaría
        //al anterior al haberse borrado un elemento, al crear la nueva lista esta no se
        //actualiza y puede llevar a cabo el funcionamiento esperado del bucle
        List<EnemyIngame> enemiesFighting = game.getEnemiesFighting();
        List<EnemyIngame> targetList = new ArrayList<>();
        for(EnemyIngame enemy : enemiesFighting){
            targetList.add(enemy);
        }
        for (EnemyIngame target : targetList) {
            new DealDamageCommand(2, playerFrom, target).execute();
        }
        //el motivo de la creación de la nueva lista es para evitar eliminar de la
        //lista de jugadores al jugador que utiiza la carta y que explote la partida
        List<Player> players = game.getPlayers();
        List<Player> targets = new ArrayList<>();
        for(Player player : players){
            if(player!=playerFrom){
                targets.add(player);
            }
        }
        for (Player target : targets) {
            new DiscardCommand(1, target).execute();
        }
    }
}
