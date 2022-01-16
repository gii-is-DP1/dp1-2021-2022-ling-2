import { Authority } from "./Authority";
import { Character } from "./Character";
import { Lobby } from "./Lobby";
import { Player } from "./Player";

export interface User {
  username: string;
  password: string;
  email: string;
  authorities: Authority[];
  lobby?: Lobby;
  player?: Player;
  character?: Character;
  enabled: boolean;
}
