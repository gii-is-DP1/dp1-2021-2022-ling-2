import { BASE_IMAGE_PATH } from "../../../constants/paths";
import { CARD_BACK } from "../../../constants/images";
import Token from "./token";

export default function MarketCard(params) {
  const { card, count } = params;

  return (
    <>
      {card ? (
        <img
          className="card hover:scale-250 hover:translate-y-32"
          src={`${BASE_IMAGE_PATH}${card.marketCard.frontImage}`}
          alt={card.marketCard.marketCardTypeEnum}
        ></img>
      ) : (
        <div
          className="flex items-center justify-center h-full bg-contain bg-no-repeat box-shadow-lg rounded-lg zoomable hover:scale-250 hover:translate-y-32"
          style={{
            backgroundImage: `url('${CARD_BACK}')`,
          }}
          alt={`Market card pile (${count} items)`}
        >
          <Token type="count" value={count} />
        </div>
      )}
    </>
  );
}
