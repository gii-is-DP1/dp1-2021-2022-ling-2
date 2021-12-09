import { CARD_BACK } from "../../../constants/images";

type Params = {
  counterclockwise?: boolean;
};
export default function PlaceholderCard(params: Params) {
  const { counterclockwise } = params;

  return (
    <img
      className={`card zoomable hover:scale-250 ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu hover:rotate-90"
          : "transform-gpu hover:-rotate-90")
      }`}
      src={CARD_BACK}
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
