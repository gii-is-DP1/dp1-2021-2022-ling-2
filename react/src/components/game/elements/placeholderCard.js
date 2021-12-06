import { CARD_BACK } from "../../../constants/images";

export default function PlaceholderCard(params) {
  return (
    <img
      className="card hover:scale-250 hover:-translate-y-20"
      src={CARD_BACK}
      // template CARD_BACK
      alt="PLACEHOLDER CARD"
    ></img>
  );
}
