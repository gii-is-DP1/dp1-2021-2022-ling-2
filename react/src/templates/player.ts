import { Player } from "../interfaces/Player";
import { templateUser } from "./user";

export const templatePlayer: Player = {
  id: 0,
  user: templateUser,
  glory: 0,
  kills: 0,
  gold: 0,
  wounds: 0,
  guard: false,
  turnOrder: 0,
  hand: [],
  abilityPile: [],
  discardPile: [],
  characterTypeEnum: undefined,
  isDead: false,
};
