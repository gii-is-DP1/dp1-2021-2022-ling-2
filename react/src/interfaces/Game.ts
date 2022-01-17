import { EnemyIngame } from "./EnemyIngame";
import { MarketCardIngame } from "./MarketCardIngame";
import { Player } from "./Player";
import { Turn } from "./Turn";

export interface Game {
  id: number;
  startTime?: number;
  finishTime?: number;
  hasScenes: boolean;
  players: Player[];
  leader: Player;
  currentTurn?: Turn;
  duration?: number;
  winner?: Player;
  enemiesInPile: EnemyIngame[];
  enemiesFighting: EnemyIngame[];
  marketCardsInPile: MarketCardIngame[];
  marketCardsForSale: MarketCardIngame[];
}
