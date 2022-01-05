package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DealDamageCommand implements Command {

    private Integer damage;
    private Player playerFrom;
    private EnemyIngame targetedEnemy;

    @Override
    public void execute() {
        Integer currentEndurance = targetedEnemy.getCurrentEndurance();
        Boolean whetstoneCondition = targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.PIEDRA_DE_AMOLAR);
        Boolean corrosiveArrowCondition = targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.FLECHA_CORROSIVA);
        if(whetstoneCondition || corrosiveArrowCondition){
            damage = damage+1;
        }

        targetedEnemy.setCurrentEndurance(currentEndurance - damage);

        Boolean dead = (currentEndurance - damage <= 0);
        if (dead) {
            targetedEnemy.setCurrentEndurance(0);
            targetedEnemy.getGame().getEnemiesFighting().remove(targetedEnemy);
            Integer playerKillCount = playerFrom.getKills();
            Integer enemyDefeatedGlory = targetedEnemy.getEnemy().getBaseGlory() + targetedEnemy.getEnemy().getExtraGlory();
            Integer enemyDefeatedGold = targetedEnemy.getEnemy().getGold();
            playerFrom.setKills(playerKillCount+1);
            new GiveGloryCommand(enemyDefeatedGlory, playerFrom).execute();
            new GiveGoldCommand(enemyDefeatedGold, playerFrom).execute();

            //este es el comit
        }
    }
}
