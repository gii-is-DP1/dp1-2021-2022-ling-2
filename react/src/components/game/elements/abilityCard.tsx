import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";

type Params = {
  card: AbilityCardIngame;
};
export default function AbilityCard(params: Params) {
  const { card } = params;
  return (
    <img
      className={`card transform-gpu
      border-2 border-yellow-300 border-opacity-0 hover:border-opacity-100
      hover:-translate-y-6 
      `}
      src={`/images/cards/characters/${card.abilityCard.characterTypeEnum.toLowerCase()}/${card.abilityCard.abilityCardTypeEnum.toLowerCase()}.png`}
      // template CARD_BACK
      alt={card.abilityCard.abilityCardTypeEnum.toLowerCase().toString()}
    ></img>
  );
}
