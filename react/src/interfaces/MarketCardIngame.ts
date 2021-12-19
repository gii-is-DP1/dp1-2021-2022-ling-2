import { MarketCardLocationEnum } from "../types/MarketCardLocationEnum";
import { MarketCard } from "./MarketCard";

export interface MarketCardIngame {
  id: number;
  marketCard: MarketCard;
  location: MarketCardLocationEnum;
}
