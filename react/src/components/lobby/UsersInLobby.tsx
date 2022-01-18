import axios from "../../api/axiosConfig";
import { useContext } from "react";
import UserContext from "../../context/user";
import capitalize from "../../helpers/capitalize";
import tokenParser from "../../helpers/tokenParser";
import { Game } from "../../interfaces/Game";
import { Player } from "../../interfaces/Player";
import { TokenUser } from "../../interfaces/TokenUser";
import toast from "react-hot-toast";

type Props = {
  game: Game;
};

export default function UsersInLobby(props: Props) {
  const { game } = props; // destructuring props // TODO in other files too
  // user: the one who is logged in
  const { userToken } = useContext(UserContext);
  const loggedUser: TokenUser = tokenParser(useContext(UserContext)); // user who is logged in

  const playerIsHost = (player: Player | null): boolean =>
    player != null && player.user.username === game.leader.user.username;

  const userIsHost = (user: TokenUser | null): boolean =>
    user != null && user.username === game.leader.user.username;

  const characters = [
    "ranger",
    "ranger",
    "rogue",
    "rogue",
    "warrior",
    "warrior",
    "wizard",
    "wizard",
  ];
  const getCharacterFromId = (id: number | undefined): string => {
    if (id === undefined) return "none";
    return characters[id - 1];
  };

  const getGenderFromId = (id: number | undefined): string => {
    if (id === undefined) return "";
    return id % 2 ? "♂ " : "♀ ";
  };

  async function removePlayerFromLobby(player: Player) {
    try {
      await axios.delete(`/games/${game.id}/remove/${player.id}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
      toast.success("Player kicked successfully");
    } catch (error: any) {
      toast.error(error?.message);
    }
  }

  return (
    <ul className="flex flex-col space-y-2">
      {game.players
        .sort((a, b) =>
          a.user.username < b.user.username
            ? 1
            : a.user.username > b.user.username
            ? -1
            : 0
        ) // arbitrary but consistent order
        .map((player: Player, idx) => (
          <li key={idx} className="bg-green-700 rounded-xl p-2 text-white">
            {!playerIsHost(player) && userIsHost(loggedUser) && (
              // show me the kick button over a player only if i'm the host
              // AND the player to kick is not me
              <button
                className="mr-2"
                onClick={(e) => removePlayerFromLobby(player)}
              >
                ❌
              </button>
            )}
            {playerIsHost(player) && <span className="m-0 p-1">👑</span>}{" "}
            {player.user.username +
              " — " +
              getGenderFromId(player.character?.id) +
              capitalize(getCharacterFromId(player.character?.id))}
          </li>
        ))}
    </ul>
  );
}
