import { EnemyLocationEnum } from "../types/EnemyLocationEnum";
import { HordeEnemy } from "./HordeEnemy";

export interface HordeEnemyIngame {
  id: number;
  hordeEnemy: HordeEnemy;
  hordeEnemyLocation: EnemyLocationEnum;
  currentEndurance: number;
}
