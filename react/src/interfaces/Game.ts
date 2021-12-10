import { Player } from "./Player";
import { Turn } from "./Turn";

export interface Game {
  id: number;
  players: Player[];
  leader: Player;
  currentTurn: Turn;
  hasScenes: boolean;
  startTime: number;
}
