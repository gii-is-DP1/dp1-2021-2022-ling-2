package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DealDamageCommand implements Command {

    private Integer damage;
    //private Player playerFrom;
    private EnemyIngame targetedEnemy;

    @Override
    public void execute() {
        Integer currentEndurance = targetedEnemy.getCurrentEndurance();
        Boolean whetstoneCondition = targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.PIEDRA_DE_AMOLAR);
        Boolean corrosiveArrow = targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.FLECHA_CORROSIVA);
        if(whetstoneCondition || corrosiveArrow){
            damage = damage+1;
        }

        targetedEnemy.setCurrentEndurance(currentEndurance - damage);

        Boolean dead = (currentEndurance - damage <= 0);
        if (dead) {
            targetedEnemy.setCurrentEndurance(0);
            targetedEnemy.getGame().getEnemiesFighting().remove(targetedEnemy);
            //Integer playerKillCount = playerFrom.getKills();
            //playerFrom.setKills(playerKillCount+1);
        }
    }
}
