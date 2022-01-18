import { Game } from "../interfaces/Game";
import { templatePlayer } from "./player";

export const templateGame: Game = {
  id: 0,
  name: "",
  hasScenes: false,
  spectatorsAllowed: false,
  maxPlayers: 2,
  players: [],
  leader: templatePlayer,
  enemiesInPile: [],
  enemiesFighting: [],
  marketCardsInPile: [],
  marketCardsForSale: [],
  comments: [],
  hasStarted: false,
  hasFinished: false,
};
