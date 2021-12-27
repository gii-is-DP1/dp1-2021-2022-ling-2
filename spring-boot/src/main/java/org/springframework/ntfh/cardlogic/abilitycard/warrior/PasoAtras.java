package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.Command;
import org.springframework.ntfh.entity.player.Player;

public class PasoAtras implements Command {
    Integer gameId;
    Player playerFrom;

    // TODO commands declaration

    // TODO autowired services declaration

    // TODO constructor for all the previous attributes

    @Override
    public void execute() {
        System.out.println(this.toString());
    }
}
