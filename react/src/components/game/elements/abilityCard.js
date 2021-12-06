import { CARD_BACK } from "../../../constants/images";

export default function abilityCard(params) {
  const { card, position, reverse } = params;
  return (
    <img
      className={`card transform-gpu
      ${reverse ? "" : "-"}translate-x-${position * 12}
      2xl:${reverse ? "" : "-"}translate-x-${position * 16}
       hover:scale-250 hover:-translate-y-20`}
      src={CARD_BACK}
      // template CARD_BACK
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
