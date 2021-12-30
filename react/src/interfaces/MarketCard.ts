import { MarketCardTypeEnum } from "../types/MarketCardTypeEnum";
import { ProficiencyTypeEnum } from "../types/ProficiencyTypeEnum";

export interface MarketCard {
  id: number;
  price: number;
  proficiencies: ProficiencyTypeEnum[];
  marketCardTypeEnum: MarketCardTypeEnum;
}
