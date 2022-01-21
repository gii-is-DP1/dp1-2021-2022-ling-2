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
 * @param { count } count number to be desplaying on the card
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
      draggable={false}
      className={
        `card zoomable flex items-center justify-center h-full bg-contain bg-no-repeat hover:scale-250 ${zoomDirectionClassname}
      ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu hover:rotate-90"
          : "transform-gpu hover:-rotate-90")
      }
        ${!count && " invisible "}` /* Don´t show the counter if count is 0 */
      }
      style={{
        backgroundImage: `url('${CARD_BACK}')`,
      }}
    >
      <Token type="count" value={count} />
    </div>
  );
}
