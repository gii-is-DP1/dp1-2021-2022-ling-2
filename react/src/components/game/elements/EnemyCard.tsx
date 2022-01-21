import { useContext } from "react";
import toast from "react-hot-toast";
import axios from "../../../api/axiosConfig";
import { BASE_IMAGE_PATH } from "../../../constants/paths";
import GameContext from "../../../context/game";
import UserContext from "../../../context/user";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";
import { EnemyIngame } from "../../../interfaces/EnemyIngame";
import Token from "./token";

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
  const { setGame } = useContext(GameContext);

  const handleOnClick = async () => {
    if (!selectedAbilityCard) return; // Do nothing if there is no selected abilityCard
    setSelectedAbilityCard(null);
    try {
      const response = await axios.post(
        `/ability-cards/${selectedAbilityCard.id}`,
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
    <div
      draggable={false}
      className="card zoomable flex items-center justify-center w-full h-full bg-cover hover:scale-250 border-3 border-black"
      onClick={handleOnClick}
      style={{
        backgroundImage: `url('${BASE_IMAGE_PATH}/cards/enemies${
          flipped
            ? "/backs/warlord.png"
            : `/fronts/${enemyIngame.enemy.enemyType.toLowerCase()}.png`
        }')`,
      }}
    >
      <div className="transform scale-50 absolute -left-2.5 -top-2.5">
        <Token type="kill" value={enemyIngame.currentEndurance} />
      </div>
    </div>
  );
}
