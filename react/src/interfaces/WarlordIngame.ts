import { EnemyLocationEnum } from "../types/EnemyLocationEnum";
import { Warlord } from "./Warlord";

export interface WarlordIngame {
  id: number;
  warlord: Warlord;
  currentEndurance: number;
  location: EnemyLocationEnum;
}
