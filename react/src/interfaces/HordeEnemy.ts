import { HordeEnemyType } from "./HordeEnemyType";

export interface HordeEnemy {
  id: number;
  hordeEnemyType: HordeEnemyType;
  gold: number;
  extraGlory: number;
  frontImage: string;
  backImage: string;
}
