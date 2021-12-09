import { User } from "./User";

export interface Lobby {
  host: User;
  users: User[];
}
