import { CARD_BACK } from "../../../constants/images";

export default function abilityCard(params) {
  const { card, position, reverse } = params;
  return (
    <img
      className={`relative transform-gpu
      ${reverse ? "" : "-"}translate-x-${position * 12}
      2xl:${reverse ? "" : "-"}translate-x-${position * 16}
      card hover:scale-250 hover:-translate-y-32`}
      src={CARD_BACK}
      // template CARD_BACK
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
