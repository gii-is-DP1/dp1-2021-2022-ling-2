import { CARD_BACK } from "../../../constants/images";
import Token from "./token";

type ZoomDirectionType = "up" | "down" | "center";

type Params = {
  count: number;
  zoomDirection?: ZoomDirectionType;
  counterclockwise?: boolean;
};

/**
 *
 * @param { count } count number to be resplaying on the card
 * @returns Component of a standard card back with a counter on top
 */
export default function CountCard(params: Params) {
  const { count, zoomDirection, counterclockwise } = params;
  let zoomDirectionClassname = "";
  if (zoomDirection === "up") {
    zoomDirectionClassname = "hover:-translate-y-20";
  } else if (zoomDirection === "down") {
    zoomDirectionClassname = "hover:translate-y-20";
  }

  return (
    <div
      style={{
        backgroundImage: `url('${CARD_BACK}')`,
      }}
      className={`card zoomable flex items-center justify-center w-full h-full bg-contain bg-no-repeat hover:scale-250 ${zoomDirectionClassname}
      ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu hover:rotate-90"
          : "transform-gpu hover:-rotate-90")
      }`}
    >
      <Token type="count" value={count} />
    </div>
  );
}