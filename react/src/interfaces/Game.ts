import { GameStateEnum } from "../types/GameStateEnum";
import { EnemyIngame } from "./EnemyIngame";
import { MarketCardIngame } from "./MarketCardIngame";
import { Player } from "./Player";
import { Turn } from "./Turn";

export interface Game {
  id: number;
  name: string;
  startTime?: number;
  finishTime?: number;
  hasScenes: boolean;
  spectatorsAllowed: boolean;
  maxPlayers: 2 | 3 | 4;
  players: Player[];
  leader: Player;
  winner?: Player;
  enemiesInPile: EnemyIngame[];
  enemiesFighting: EnemyIngame[];
  marketCardsInPile: MarketCardIngame[];
  marketCardsForSale: MarketCardIngame[];
  comments: Comment[];
  currentTurn?: Turn;
  duration?: number;
  hasStarted: boolean;
  hasFinished: boolean;
  stateType: GameStateEnum;
}
