import { useContext, useEffect, useState } from "react";
import axios from "../../../api/axiosConfig";
import gameContext from "../../../context/game";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import CountCard from "../elements/countCard";
import EnemyCard from "../elements/EnemyCard";
import MarketCard from "../elements/marketCard";
import PlaceholderCard from "../elements/placeholderCard";
import SceneCard from "../elements/sceneCard";

type Params = {
  selectedAbilityCard: AbilityCardIngame | null;
  setSelectedAbilityCard: (abilityCard: AbilityCardIngame | null) => void;
};

export default function CenterZone(params: Params) {
  const { selectedAbilityCard, setSelectedAbilityCard } = params;
  const [sceneCount, setSceneCount] = useState<number>(0);
  const { game } = useContext(gameContext);
  const marketCardsInPile = game?.marketCardsInPile;
  const marketCardsForSale = game?.marketCardsForSale;
  const enemiesInPile = game?.enemiesInPile;
  const enemiesFighting = game?.enemiesFighting;

  useEffect(() => {
    const fetchSceneCount = async () => {
      // Total number of scenes
      const response = await axios.get("/scenes/count");
      setSceneCount(response.data);
    };
    fetchSceneCount();
  }, []);

  return (
    <div className="grid grid-rows-3 place-items-center">
      <span className="grid grid-cols-6 gap-2 py-1">
        {/* Market cards pile (facing down) */}
        <span className="col-start-1">
          <CountCard
            count={marketCardsInPile?.length ?? 0}
            zoomDirection="down"
          />
        </span>
        {/* Available market cards (facing up) */}
        {marketCardsForSale?.map((card) => (
          <MarketCard key={card.id} card={card} />
        ))}
      </span>
      <span className="grid grid-cols-6 gap-2 py-1">
        <span className="col-start-3">
          {/* TODO replace with a flipped warlord card */}
          <PlaceholderCard />
        </span>
        {game?.hasScenes && (
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
        <span className="invisible">
          {/* Blank space (first col) */}
          <PlaceholderCard />
        </span>
        {/* Up to 3 horde enemies and up to 1 Warlord */}
        {enemiesFighting?.map((enemy) => (
          <EnemyCard
            key={enemy.id}
            enemyIngame={enemy}
            selectedAbilityCard={selectedAbilityCard}
            setSelectedAbilityCard={setSelectedAbilityCard}
          />
        ))}
      </span>
    </div>
  );
}
