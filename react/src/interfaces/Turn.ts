import { TurnStateEnum } from "../types/TurnStateEnum";
import { Game } from "./Game";
import { Player } from "./Player";
import { Scene } from "./Scene";

export interface Turn {
  game: Game;
  player: Player;
  currentScene: Scene;
  stateType: TurnStateEnum;
}
