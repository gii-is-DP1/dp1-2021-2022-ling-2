import { CARD_BACK } from "../../../constants/images";

type Params = {
  counterclockwise?: boolean;
  position?: number;
  reverse?: boolean;
};
export default function PlaceholderCard(params: Params) {
  const { counterclockwise } = params;

  // TODO replace position translate with -space-x-12 in the renderer
  return (
    <img
      draggable={false}
      className={`card transform-gpu ${
        counterclockwise !== undefined &&
        (counterclockwise ? "hover:rotate-90" : "hover:-rotate-90")
      }
      `}
      src={CARD_BACK}
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
