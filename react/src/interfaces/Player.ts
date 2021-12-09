import { Character } from "./Character";
import { User } from "./User";

export interface Player {
  user: User;
  glory: number;
  kills: number;
  gold: number;
  wounds: number;
  turnOrder: number;
  characterType: Character;
}
