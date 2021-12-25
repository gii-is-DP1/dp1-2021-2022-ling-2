import select from "../../../context/select";
import { useContext } from "react";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";

type Params = {
  card: AbilityCardIngame;
};

export default function AbilityCard(params: Params) {
  const { card } = params;
  const { selected, addSelect, removeSelect } = useContext(select);
  const isSelected = selected.includes(card);

  const abilityCardTypeEnum = card.abilityCard.abilityCardTypeEnum;

  return (
    <img
      className={`card transform-gpu
      border-2 border-opacity-0
      hover:-translate-y-6 
      ${isSelected ? "border-opacity-100 border-yellow-300" : ""}
      `}
      src={`/images/cards/characters/${card.abilityCard.characterTypeEnum.toLowerCase()}/${abilityCardTypeEnum.toLowerCase()}.png`}
      alt={abilityCardTypeEnum.toLowerCase().toString()}
      onClick={() => (isSelected ? removeSelect(card) : addSelect(card))}
    ></img>
  );
}
