import { Game } from "./Game";
import { HordeEnemy } from "./HordeEnemy";

export interface HordeEnemyIngame {
  id: number;
  hordeEnemy: HordeEnemy;
  currentEndurance: number;
  game: Game;
}
