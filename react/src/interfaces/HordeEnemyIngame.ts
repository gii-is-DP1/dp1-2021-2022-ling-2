import { EnemyLocationEnum } from "../types/EnemyLocationEnum";
import { Game } from "./Game";
import { HordeEnemy } from "./HordeEnemy";

export interface HordeEnemyIngame {
  id: number;
  hordeEnemy: HordeEnemy;
  hordeEnemyLocation: EnemyLocationEnum;
  currentEndurance: number;
  game: Game;
}
