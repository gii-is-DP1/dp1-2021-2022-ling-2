import { Authority } from "./Authority";
import { Game } from "./Game";
import { Player } from "./Player";

export interface User {
  username: string;
  password: string;
  email: string;
  enabled: boolean;
  game?: Game;
  player?: Player;
  authorities: Authority[];
}
