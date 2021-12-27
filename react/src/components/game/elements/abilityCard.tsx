import { useContext } from "react";
import toast from "react-hot-toast";
import { useParams } from "react-router";
import axios from "../../../api/axiosConfig";
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

  const isSelected = selected === card;
  const isSelfPlayable = SelfPlayableCards.includes(
    card.abilityCard.abilityCardTypeEnum
  );

  const handleOnClick = async () => {
    if (isSelfPlayable) {
      try {
        const response = await axios.post(
          `/games/${gameId}/ability-cards/${card.id}`,
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
      className={`card transform-gpu
      border-2 border-opacity-0
      hover:-translate-y-6 
      ${isSelected ? "border-opacity-100 border-yellow-300" : ""}
      `}
      src={`/images/cards/characters/${card.abilityCard.characterTypeEnum.toLowerCase()}/${abilityCardTypeEnum.toLowerCase()}.png`}
      alt={abilityCardTypeEnum.toLowerCase().toString()}
      onClick={handleOnClick}
    ></img>
  );
}
