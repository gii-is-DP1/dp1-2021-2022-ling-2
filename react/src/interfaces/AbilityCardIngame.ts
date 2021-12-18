import { AbilityCardLocationEnum } from "../types/AbilityCardLocationEnum";
import { AbilityCard } from "./AbilityCard";

export interface AbilityCardIngame {
  abilityCard: AbilityCard;
  location: AbilityCardLocationEnum;
}
