import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";

type Params = {
  card: AbilityCardIngame;
};

export default function AbilityCard(params: Params) {
  const { card } = params;

  const abilityCardTypeEnum = card.abilityCard.abilityCardTypeEnum;

  return (
    <img
      className={`card transform-gpu
      border-2 border-yellow-300 border-opacity-0 hover:border-opacity-100
      hover:-translate-y-6 
      `}
      src={`/images/cards/characters/${card.abilityCard.characterTypeEnum.toLowerCase()}/${abilityCardTypeEnum.toLowerCase()}.png`}
      alt={abilityCardTypeEnum.toLowerCase().toString()}
    ></img>
  );
}
