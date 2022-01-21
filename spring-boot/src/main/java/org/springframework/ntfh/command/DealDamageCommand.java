package org.springframework.ntfh.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ntfh.entity.enemy.EnemyType;
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
        Integer playerKillCount = playerFrom.getKills();
        Boolean whetstoneCondition =
                targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.PIEDRA_DE_AMOLAR);
        Boolean corrosiveArrow =
                targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.FLECHA_CORROSIVA);
        if (whetstoneCondition || corrosiveArrow) {
            damage++;
        }

        List<EnemyType> warlords = List.of(EnemyType.GURDRUG, EnemyType.ROGHKILLER, EnemyType.SHRIEKKNIFER);
        EnemyType targetedEnemyType = targetedEnemy.getEnemy().getEnemyType();

        if (warlords.contains(targetedEnemyType)) {
            new GiveGloryCommand(1, playerFrom).execute();
            if(targetedEnemyType.equals(warlords.get(0))){
                new DiscardCommand(1, playerFrom).execute();
            }
        }

        List<EnemyType> enemiesFightingTypesList = new ArrayList<>();
        for(EnemyIngame enemy:playerFrom.getGame().getEnemiesFighting()){
            enemiesFightingTypesList.add(enemy.getEnemy().getEnemyType());
        }

        if (enemiesFightingTypesList.contains(EnemyType.SHRIEKKNIFER) && damage==1){
            new GiveGloryCommand(1, playerFrom).execute();
        }

        targetedEnemy.setCurrentEndurance(currentEndurance - damage);
        if (Boolean.TRUE.equals(targetedEnemy.isDead())) {
            targetedEnemy.getGame().getEnemiesFighting().remove(targetedEnemy);
            playerFrom.setKills(playerKillCount + 1);

            if (targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.TRAMPA)) {
                Integer enemyDefeatedGlory = targetedEnemy.getEnemy().getBaseGlory();
                new GiveGloryCommand(enemyDefeatedGlory, playerFrom).execute();

            } else {

                Integer enemyDefeatedGlory =
                        targetedEnemy.getEnemy().getBaseGlory() + targetedEnemy.getEnemy().getExtraGlory();
                Integer enemyDefeatedGold = targetedEnemy.getEnemy().getGold();
                new GiveGloryCommand(enemyDefeatedGlory, playerFrom).execute();
                new GiveGoldCommand(enemyDefeatedGold, playerFrom).execute();
            }
        }
    }
}
