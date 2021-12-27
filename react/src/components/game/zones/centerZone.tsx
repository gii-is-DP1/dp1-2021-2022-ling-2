import { useEffect, useState } from "react";
import axios from "../../../api/axiosConfig";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import { Game } from "../../../interfaces/Game";
import { HordeEnemyIngame } from "../../../interfaces/HordeEnemyIngame";
import { MarketCardIngame } from "../../../interfaces/MarketCardIngame";
import { WarlordIngame } from "../../../interfaces/WarlordIngame";
import CountCard from "../elements/countCard";
import HordeEnemyCard from "../elements/hordeEnemyCard";
import MarketCard from "../elements/marketCard";
import PlaceholderCard from "../elements/placeholderCard";
import SceneCard from "../elements/sceneCard";

type Params = {
  game: Game;
  selectedAbilityCard: AbilityCardIngame | null;
  setSelectedAbilityCard: (abilityCard: AbilityCardIngame | null) => void;
};

export default function CenterZone(params: Params) {
  const { game, selectedAbilityCard, setSelectedAbilityCard } = params;
  const [marketCards, setMarketCards] = useState<MarketCardIngame[]>([]);
  const [hordeEnemies, setHordeEnemies] = useState<HordeEnemyIngame[]>([]);
  const [sceneCount, setSceneCount] = useState<number>(0);
  const [warlord, setWarlord] = useState<WarlordIngame | undefined>(undefined);

  useEffect(() => {
    const fetchmarketCards = async () => {
      const response = await axios.get(`/market-cards/${params.game.id}`);
      setMarketCards(response.data);
    };
    const fetchWarlord = async () => {
      const response = await axios.get(`/warlords/${params.game.id}`);
      setWarlord(response.data);
    };
    const fetchHordeEnemies = async () => {
      const response = await axios.get(`/horde-enemies/${params.game.id}`);
      setHordeEnemies(response.data);
    };
    const fetchSceneCount = async () => {
      // Total number of scenes
      const response = await axios.get("/scenes/count");
      setSceneCount(response.data);
    };
    fetchSceneCount();
    fetchmarketCards();
    fetchWarlord();
    fetchHordeEnemies();
  }, []);

  return (
    <div className="grid grid-rows-3 place-items-center">
      <span className="grid grid-cols-6 gap-2 py-1">
        {/* Market cards pile (facing down) */}
        <span className="col-start-1">
          <CountCard
            count={
              marketCards.filter((card) => card.location === "MARKET_PILE")
                .length
            }
            zoomDirection="down"
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
              <SceneCard scene={game?.currentTurn?.currentScene} />
            </span>
          </>
        )}
      </span>
      <span className="grid grid-cols-6 gap-2 py-1">
        <span> {/* Blank space (first col) */} </span>
        {/* Up to 3 horde enemies and up to 1 Warlord */}
        {hordeEnemies
          .filter((enemy) => enemy.hordeEnemyLocation === "FIGHTING")
          .map((enemy) => (
            <HordeEnemyCard
              key={enemy.id}
              enemy={enemy}
              selectedAbilityCard={selectedAbilityCard}
              setSelectedAbilityCard={setSelectedAbilityCard}
            />
          ))}
      </span>
    </div>
  );
}
