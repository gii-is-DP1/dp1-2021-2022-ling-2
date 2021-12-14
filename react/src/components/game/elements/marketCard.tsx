import { CARD_BACK } from "../../../constants/images";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import { MarketCardIngame } from "../../../interfaces/MarketCardIngame";
import Token from "./token";

type Params = {
  card?: MarketCardIngame;
  count?: number;
};

export default function MarketCard(params: Params) {
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
        count && (
          <div
            className="card zoomable flex items-center justify-center h-full bg-contain bg-no-repeat hover:scale-250 hover:translate-y-20"
            style={{
              backgroundImage: `url('${CARD_BACK}')`,
            }}
          >
            <Token type="count" value={count} />
          </div>
        )
      )}
    </>
  );
}
