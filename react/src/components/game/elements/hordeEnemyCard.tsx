import { useContext } from "react";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import select from "../../../context/select";
import { HordeEnemyIngame } from "../../../interfaces/HordeEnemyIngame";

type Params = {
  enemy: HordeEnemyIngame;
  flipped?: boolean;
};

export default function HordeEnemyCard(params: Params) {
  const { enemy, flipped } = params;
  const { selected, addSelect, removeSelect } = useContext(select);
  const isSelected = selected.includes(enemy);

  return (
    <img
      className={`card zoomable hover:scale-250
      border-2 border-opacity-0
      ${isSelected ? "border-opacity-100 border-red-300" : ""}
      `}
      src={
        BASE_IMAGE_PATH +
        (flipped ? enemy.hordeEnemy.backImage : enemy.hordeEnemy.frontImage)
      } // TODO frontend or backend?
      alt={enemy.hordeEnemy.hordeEnemyType.hordeEnemyTypeEnum}
      onClick={() => (isSelected ? removeSelect(enemy) : addSelect(enemy))}
    ></img>
  );
}
