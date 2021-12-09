import { Player } from "./Player";

export interface Game {
  id: string;
  players: Player[];
  leader: Player;
  hasScenes: boolean;
  startTime: number;
}
