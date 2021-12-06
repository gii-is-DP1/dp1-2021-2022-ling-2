import { CARD_BACK } from "../../../constants/images";

export default function abilityCard(params) {
  const { card, position, reverse } = params;
  return (
    <img
      className={`relative z-${position} transform ${
        reverse ? "" : "-"
      }translate-x-${position * 12} card hover:scale-250 hover:-translate-y-32`}
      src={CARD_BACK}
      // template CARD_BACK
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
