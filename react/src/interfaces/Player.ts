import { CharacterTypeEnum } from "../types/CharacterTypeEnum";
import { AbilityCardIngame } from "./AbilityCardIngame";
import { Character } from "./Character";
import { User } from "./User";

export interface Player {
  id: number;
  user: User;
  glory: number;
  kills: number;
  gold: number;
  wounds: number;
  guard: boolean;
  turnOrder: number;
  character?: Character;
  hand: AbilityCardIngame[];
  abilityPile: AbilityCardIngame[];
  discardPile: AbilityCardIngame[];
  characterTypeEnum: CharacterTypeEnum;
  isDead: boolean;
}
