import { EnemyIngame } from "./EnemyIngame";
import { MarketCardIngame } from "./MarketCardIngame";
import { Player } from "./Player";
import { Turn } from "./Turn";

export interface Game {
  id: number;
  startTime: number;
  hasScenes: boolean;
  players: Player[];
  leader: Player;
  currentTurn: Turn;
  enemiesInPile: EnemyIngame[];
  enemiesFighting: EnemyIngame[];
  marketCardsInPile: MarketCardIngame[];
  marketCardsForSale: MarketCardIngame[];
}
