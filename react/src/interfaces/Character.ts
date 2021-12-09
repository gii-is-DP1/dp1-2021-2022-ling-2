import { CharacterGenderEnum } from "../types/CharacterGenderEnum";
import { CharacterTypeEnum } from "../types/CharacterTypeEnum";

export interface Character {
  id: number;
  characterTypeEnum: CharacterTypeEnum;
  characterGenderEnum: CharacterGenderEnum;
}
