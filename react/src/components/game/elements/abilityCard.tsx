import { useContext } from "react";
import toast from "react-hot-toast";
import { useParams } from "react-router";
import axios from "../../../api/axiosConfig";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import GameContext from "../../../context/game";
import UserContext from "../../../context/user";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import { SelfPlayableCards } from "../../../types/SelfPlayableCardEnum";

type Params = {
  card: AbilityCardIngame;
  selected: AbilityCardIngame | null;
  setSelected: (card: AbilityCardIngame | null) => void;
};

export default function AbilityCard(params: Params) {
  const { card, selected, setSelected } = params;
  const { userToken } = useContext(UserContext);
  const { setGame } = useContext(GameContext);
  const { gameId } = useParams<{ gameId: string }>(); // get params from react router link

  const abilityCardTypeEnum = card.abilityCard.abilityCardTypeEnum;
  const characterTypeEnum = card.abilityCard.characterTypeEnum;

  const isSelected = selected === card;
  const isSelfPlayable = SelfPlayableCards.includes(
    card.abilityCard.abilityCardTypeEnum
  );

  const playCard = async () => {
    if (isSelfPlayable) {
      try {
        const response = await axios.post(
          `/ability-cards/${card.id}`,
          { enemyId: null }, // Payload
          { headers: { Authorization: "Bearer " + userToken } }
        );
        const _game = response.data;
        setGame(_game);
      } catch (error: any) {
        toast.error(error?.message);
      }
    } else {
      isSelected ? setSelected(null) : setSelected(card);
    }
  };

  return (
    <img
      draggable={false}
      className={`card transform-gpu
      border-2 border-opacity-0
      hover:-translate-y-6 
      ${isSelected ? "border-opacity-100 border-yellow-300" : ""}
      `}
      src={`${BASE_IMAGE_PATH}/cards${
        characterTypeEnum
          ? "/characters/" + characterTypeEnum.toLowerCase()
          : "/items"
      }/${abilityCardTypeEnum.toLowerCase()}.png`}
      alt={abilityCardTypeEnum.toLowerCase().toString()}
      onClick={playCard}
    ></img>
  );
}
