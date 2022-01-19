import { Authority } from "./Authority";
import { Player } from "./Player";

export interface User {
  username: string;
  password: string;
  email: string;
  enabled: boolean;
  player?: Player;
  authorities: Authority[];
}
