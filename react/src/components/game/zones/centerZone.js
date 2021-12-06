import { useEffect, useState } from "react";
import axios from "../../../api/axiosConfig";
import MarketCard from "../elements/marketCard";
import PlaceholderCard from "../elements/placeholderCard";
import SceneCard from "../elements/sceneCard";

export default function CenterZone(params) {
  const { game } = params;
  const [marketCards, setMarketCards] = useState([]);
  const [hordeEnemies, setHordeEnemies] = useState([]);
  const [sceneCount, setSceneCount] = useState(0);

  useEffect(() => {
    console.log(game);
    const fetchmarketCards = async () => {
      const response = await axios.get(`/market-cards/${params.game.id}`);
      setMarketCards(response.data);
    };
    const fetchHordeEnemies = async () => {
      const response = await axios.get(`/horde-enemies/${params.game.id}`);
      setHordeEnemies(response.data);
      console.log(response.data);
    };
    const fetchSceneCount = async () => {
      // Total number of scenes
      const response = await axios.get("/scenes/count");
      setSceneCount(response.data);
    };
    fetchSceneCount();
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
        {game.hasScenes && (
          <>
            <span className="col-start-4 col-end-5 transform-gpu rotate-90 translate-x-8">
              {/* Scene pile*/}
              <SceneCard count={sceneCount} />
            </span>
            <span className="col-start-6 transform-gpu rotate-90">
              {/* CURRENT SCENE (FACING UP) */}
              <SceneCard scene={game.currentTurn.currentScene} />
            </span>
          </>
        )}
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
