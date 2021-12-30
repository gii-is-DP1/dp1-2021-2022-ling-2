import { EnemyCategoryTypeEnum } from "../types/EnemyCategoryTypeEnum";
import { EnemyModifierTypeEnum } from "../types/EnemyModifierTypeEnum";
import { EnemyTypeEnum } from "../types/EnemyTypeEnum";

export interface Enemy {
  id: number;
  enemyCategoryType: EnemyCategoryTypeEnum;
  enemyType: EnemyTypeEnum;
  enemyModifierType: EnemyModifierTypeEnum;
  endurance: number;
  gold: number;
  extraGlory: number;
}
