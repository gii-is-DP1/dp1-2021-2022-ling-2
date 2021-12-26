import { BASE_IMAGE_PATH } from "../../../constants/paths";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import { HordeEnemyIngame } from "../../../interfaces/HordeEnemyIngame";

type Params = {
  enemy: HordeEnemyIngame;
  flipped?: boolean;
  selectedAbilityCard: AbilityCardIngame | null;
  setSelectedAbilityCard: (abilityCard: AbilityCardIngame | null) => void;
};

export default function HordeEnemyCard(params: Params) {
  const { enemy, flipped, selectedAbilityCard, setSelectedAbilityCard } =
    params;

  const handleOnClick = () => {
    if (!selectedAbilityCard) return; // Do nothing if there is no selected abilityCard
    setSelectedAbilityCard(null);

    // playCard(card, enemy);
  };

  return (
    <img
      className={`card zoomable hover:scale-250
      border-2 border-opacity-0
      `}
      src={
        BASE_IMAGE_PATH +
        (flipped ? enemy.hordeEnemy.backImage : enemy.hordeEnemy.frontImage)
      } // TODO frontend or backend?
      alt={enemy.hordeEnemy.hordeEnemyType.hordeEnemyTypeEnum}
      onClick={handleOnClick}
    ></img>
  );
}
