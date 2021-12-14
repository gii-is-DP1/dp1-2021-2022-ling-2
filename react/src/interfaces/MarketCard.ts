import { MarketCardTypeEnum } from "../types/MarketCardTypeEnum";

export interface MarketCard {
  id: number;
  marketCardTypeEnum: MarketCardTypeEnum;
  frontImage: string;
  backImage: string;
}
