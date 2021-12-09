import { Game } from "./Game";
import { Player } from "./Player";

export interface GameHistory {
  id: number;
  duration: number;
  startTime: number;
  finishTime: number;
  winner: Player;
  game: Game;
}
