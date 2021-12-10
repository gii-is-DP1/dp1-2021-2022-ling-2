import { Game } from "./Game";
import { MarketCard } from "./MarketCard";
import { MarketCardLocationEnum } from "../types/MarketCardLocationEnum";

export interface MarketCardIngame {
  id: number;
  marketCard: MarketCard;
  location: MarketCardLocationEnum;
  game: Game;
}
