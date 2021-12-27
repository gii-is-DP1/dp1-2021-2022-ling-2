import { useContext } from "react";
import toast from "react-hot-toast";
import { useParams } from "react-router";
import axios from "../../../api/axiosConfig";
import { CARD_BACK } from "../../../constants/images";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import GameContext from "../../../context/game";
import UserContext from "../../../context/user";
import { MarketCardIngame } from "../../../interfaces/MarketCardIngame";
import Token from "./token";

type Params = {
  card?: MarketCardIngame;
  count?: number;
};

export default function MarketCard(params: Params) {
  const { card, count } = params;
  const { gameId } = useParams<{ gameId: string }>();
  const { userToken } = useContext(UserContext);
  const { setGame } = useContext(GameContext);

  const buyMarketCard = async () => {
    try {
      const response = await axios.post(
        `/games/${gameId}/market-cards/${card?.id}`,
        {},
        { headers: { Authorization: "Bearer " + userToken } }
      );
      const _game = response.data;
      setGame(_game);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  return (
    <>
      {card ? (
        <img
          className="card zoomable hover:scale-250 hover:translate-y-20"
          src={`${BASE_IMAGE_PATH}/cards/items/${card.marketCard.marketCardTypeEnum.toLowerCase()}.png`}
          alt={card.marketCard.marketCardTypeEnum}
          onClick={buyMarketCard}
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
