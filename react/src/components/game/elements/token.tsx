import { TokenTypeEnum } from "../../../types/TokenTypeEnum";

type Params = {
  type: TokenTypeEnum;
  value: number;
  counterclockwise?: boolean;
};

export default function Token(params: Params) {
  const { type, value, counterclockwise } = params;
  return (
    <div
      draggable={false}
      className={`hover:z-50 flex items-center justify-center token token-${type} hover:scale-200 ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu -rotate-90"
          : "transform-gpu rotate-90")
      }`}
    >
      <p>{value}</p>
    </div>
  );
}
