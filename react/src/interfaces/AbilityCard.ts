import { CharacterTypeEnum } from "../types/CharacterTypeEnum";

export interface AbilityCard {
  id: number;
  abilityCardTypeEnum: string;
  characterTypeEnum: CharacterTypeEnum;
}
