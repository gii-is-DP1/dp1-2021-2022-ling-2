import { CARD_BACK } from "../../../constants/images";

// TODO specify param type. this component is still in progress
export default function abilityCard(params: any) {
  const { card, position, reverse } = params;
  return (
    <img
      className={`card transform-gpu
      ${reverse ? "" : "-"}translate-x-${position * 12}
      2xl:${reverse ? "" : "-"}translate-x-${position * 16}
       ${
         position === undefined
           ? "zoomable hover:scale-250 hover:-translate-y-20"
           : ""
       } `}
      src={CARD_BACK}
      // template CARD_BACK
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
