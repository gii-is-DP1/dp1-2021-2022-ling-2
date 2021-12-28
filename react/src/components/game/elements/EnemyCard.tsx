import { useContext } from "react";
import toast from "react-hot-toast";
import { useParams } from "react-router";
import axios from "../../../api/axiosConfig";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import UserContext from "../../../context/user";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import { EnemyIngame } from "../../../interfaces/EnemyIngame";
import GameContext from "../../../context/game";

type Params = {
  enemyIngame: EnemyIngame;
  flipped?: boolean;
  selectedAbilityCard: AbilityCardIngame | null;
  setSelectedAbilityCard: (abilityCard: AbilityCardIngame | null) => void;
};

export default function EnemyCard(params: Params) {
  const { enemyIngame, flipped, selectedAbilityCard, setSelectedAbilityCard } =
    params;
  const { userToken } = useContext(UserContext);
  const { gameId } = useParams<{ gameId: string }>(); // get params from react router link
  const { setGame } = useContext(GameContext);

  const handleOnClick = async () => {
    if (!selectedAbilityCard) return; // Do nothing if there is no selected abilityCard
    setSelectedAbilityCard(null);
    try {
      const response = await axios.post(
        `/games/${gameId}/ability-cards/${selectedAbilityCard.id}`,
        { enemyId: enemyIngame.id }, // Payload
        { headers: { Authorization: "Bearer " + userToken } }
      );
      const _game = response.data;
      setGame(_game);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  return (
    <img
      className={`card zoomable hover:scale-250
      border-2 border-opacity-0
      `}
      src={`${BASE_IMAGE_PATH}/cards/enemies${
        flipped
          ? "/backs/warlord.png"
          : `/fronts/${enemyIngame.enemy.enemyType.toLowerCase()}.png`
      }`}
      alt={enemyIngame.enemy.enemyType}
      onClick={handleOnClick}
    ></img>
  );
}
