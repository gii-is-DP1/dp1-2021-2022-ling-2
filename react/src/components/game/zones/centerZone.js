import { useEffect, useState } from "react";
import axios from "../../../api/axiosConfig";
import MarketCard from "../elements/marketCard";

export default function CenterZone(params) {
  const { gameId } = params;
  const [marketCards, setMarketCards] = useState([]);
  const [hordeEnemies, setHordeEnemies] = useState([]);

  useEffect(() => {
    const fetchmarketCards = async () => {
      const response = await axios.get(`/market-cards/${params.gameId}`);
      setMarketCards(response.data);
      console.log(response.data);
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
    <div className="grid grid-rows-3">
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
      <span className="bg-green-100 grid grid-cols-6 gap-2 py-1">
        <span className="col-start-3 bg-green-400">
          {/* HordeEnemy pile (facing up), warlord card on top a bit displaced (facing down) */}
          HORDE ENEMY PILE (FACING UP)
        </span>
        <span className="col-start-5 bg-green-400">
          {/* Scene pile, Current scene */}
          CURRENT SCENE (FACING UP)
        </span>
        <span className="bg-green-400"> SCENE PILE (FACING DOWN) </span>
      </span>
      <span className="bg-blue-100 grid grid-cols-6 gap-2 py-1">
        {/* Fighting HordeEnemies (0 to 3) and optionally a warlord (0 to 1) */}
        <span className="col-start-2 bg-blue-400">HORDE ENEMY 1</span>
        <span className="bg-blue-400">HORDE ENEMY 2</span>
        <span className="bg-blue-400">HORDE ENEMY 3</span>
        <span className="bg-blue-400">WARLORD</span>
      </span>
    </div>
  );
}
