import { Authority } from "./Authority";
import { Character } from "./Character";

export interface User {
  username: string;
  password: string;
  email: string;
  authorities: Authority[];
  character: Character;
  enabled: boolean;
}
