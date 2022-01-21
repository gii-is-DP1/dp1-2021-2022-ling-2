package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.stream.IntStream;

import org.springframework.ntfh.command.ExileCommand;
import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * (Exiliable) Daño: - Todos los Héores recuperan 2 cartas. Eliminas 1 herida de tu Héroe.
 * 
 * @author Pablosancval
 */
@Component
public class OrbeCurativo {
    public void execute(Player playerFrom) {
        new HealCommand(playerFrom).execute();
        Game game = playerFrom.getGame();
        game.getPlayers().forEach(player -> IntStream.range(0, 2).forEach(i -> new RecoverCommand(player).execute()));
        new ExileCommand(playerFrom, AbilityCardTypeEnum.ORBE_CURATIVO).execute();
    }
}
