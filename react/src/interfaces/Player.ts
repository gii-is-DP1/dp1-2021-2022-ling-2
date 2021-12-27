import { AbilityCardIngame } from "./AbilityCardIngame";
import { Character } from "./Character";
import { User } from "./User";

export interface Player {
  id: number;
  glory: number;
  kills: number;
  gold: number;
  wounds: number;
  turnOrder: number;
  user: User;
  characterType: Character;
  hand: AbilityCardIngame[];
  discardPile: AbilityCardIngame[];
  abilityPile: AbilityCardIngame[];
}
