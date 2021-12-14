import { Authority } from "./Authority";
import { Character } from "./Character";
import { Lobby } from "./Lobby";

export interface User {
  username: string;
  password: string;
  email: string;
  authorities: Authority[];
  lobby: Lobby;
  character: Character;
  enabled: boolean;
}
