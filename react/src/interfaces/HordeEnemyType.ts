import { HordeEnemyTypeEnum } from "../types/HordeEnemyTypeEnum";
import { HordeModifierTypeEnum } from "../types/HordeModifierTypeEnum";

export interface HordeEnemyType {
  hordeEnemyTypeEnum: HordeEnemyTypeEnum;
  hordeModifierTypeEnum: HordeModifierTypeEnum;
  endurance: number;
}
