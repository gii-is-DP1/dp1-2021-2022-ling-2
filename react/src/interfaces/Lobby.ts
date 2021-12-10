import { Game } from "./Game";
import { User } from "./User";

export interface Lobby {
  id: number;
  maxPlayers: 2 | 3 | 4;
  name: string;
  hasScenes: boolean;
  spectatorsAllowed: boolean;
  host: User;
  users: User[];
  game: Game;
}
