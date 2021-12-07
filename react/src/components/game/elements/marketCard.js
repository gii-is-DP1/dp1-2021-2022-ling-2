import { BASE_IMAGE_PATH } from "../../../constants/paths";
import { CARD_BACK } from "../../../constants/images";
import Token from "./token";

export default function MarketCard(params) {
  const { card, count } = params;

  return (
    <>
      {card ? (
        <img
          className="card zoomable hover:scale-250 hover:translate-y-20"
          src={`${BASE_IMAGE_PATH}${card.marketCard.frontImage}`}
          alt={card.marketCard.marketCardTypeEnum}
        ></img>
      ) : (
        <div
          className="card zoomable flex items-center justify-center h-full bg-contain bg-no-repeat hover:scale-250 hover:translate-y-20"
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
