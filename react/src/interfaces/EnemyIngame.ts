import { Enemy } from "./Enemy";

export interface EnemyIngame {
  id: number;
  enemy: Enemy;
  currentEndurance: number;
}
