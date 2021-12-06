import { useEffect, useState } from "react";
import axios from "../../../api/axiosConfig";
import MarketCard from "../elements/marketCard";
import PlaceholderCard from "../elements/placeholderCard";

export default function CenterZone(params) {
  const { gameId } = params;
  const [marketCards, setMarketCards] = useState([]);
  const [hordeEnemies, setHordeEnemies] = useState([]);

  useEffect(() => {
    const fetchmarketCards = async () => {
      const response = await axios.get(`/market-cards/${params.gameId}`);
      setMarketCards(response.data);
    };
    const fetchHordeEnemies = async () => {
      const response = await axios.get(`/horde-enemies/${params.gameId}`);
      setHordeEnemies(response.data);
      console.log(response.data);
    };
    fetchmarketCards();
    fetchHordeEnemies();
  }, []);

  return (
    <div className="grid grid-rows-3 place-items-center">
      <span className="grid grid-cols-6 gap-2 py-1">
        {/* Market cards pile (facing down) */}
        <span className="col-start-1">
          <MarketCard
            count={
              marketCards.filter((card) => card.location === "MARKET_PILE")
                .length
            }
          />
        </span>
        {/* Available market cards (facing up) */}
        {marketCards
          .filter((card) => card.location === "MARKET_FOR_SALE")
          .map((card) => (
            <MarketCard key={card.id} card={card} />
          ))}
      </span>
      <span className="grid grid-cols-6 gap-2 py-1">
        <span className="col-start-3">
          {/* HordeEnemy pile (facing up), warlord card on top a bit displaced (facing down) */}
          <PlaceholderCard />
        </span>
        <span className="col-start-4 col-end-5 place-self-center transform-gpu rotate-90 translate-x-8">
          {/* Scene pile, Current scene */}
          {/* CURRENT SCENE (FACING UP) */}
          <PlaceholderCard />
        </span>
        <span className="col-start-6 transform-gpu rotate-90">
          <PlaceholderCard />
        </span>
      </span>
      <span className="grid grid-cols-6 gap-2 py-1">
        {/* Fighting HordeEnemies (0 to 3) and optionally a warlord (0 to 1) */}
        <span className="col-start-2">
          {/* HORDE ENEMY 1 */}
          <PlaceholderCard />
        </span>
        <span>
          {/* HORDE ENEMY 2 */}
          <PlaceholderCard />
        </span>
        <span>
          {/* HORDE ENEMY 3 */}
          <PlaceholderCard />
        </span>
        <span>
          {/* WARLORD */}
          <PlaceholderCard />
        </span>
      </span>
    </div>
  );
}
