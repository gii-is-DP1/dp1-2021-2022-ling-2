import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import PlayerHand from "../components/game/zones/playerHand";
import CenterZone from "../components/game/zones/centerZone";
import PlayerZoneHorizontal from "../components/game/zones/playerZoneHorizontal";
import PlayerZoneVertical from "../components/game/zones/playerZoneVertical";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Game as IGame } from "../interfaces/Game";
import { Player } from "../interfaces/Player";
import { User } from "../interfaces/User";
import selectContext from "../context/select";
import getCardType from "../helpers/getCardType";
import { HordeEnemyIngame } from "../interfaces/HordeEnemyIngame";
import { WarlordIngame } from "../interfaces/WarlordIngame";
import { AbilityCardIngame } from "../interfaces/AbilityCardIngame";

/**
 *
 * @author andrsdt
 */
export default function Game() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));

  const { gameId } = useParams<{ gameId: string }>(); // get params from react router link
  const [game, setGame] = useState<IGame | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [players, setPlayers] = useState<Player[]>([]);
  const [selected, setSelected] = useState<any[]>([]);

  const addSelect = (e: any) => {
    const parameterCardType = getCardType(e);
    const selectedCardTypes = selected.map((card) => getCardType(card));
    let selectedCopy = [...selected];

    // If there is already a card selected of the same type, remove the former
    // so there are not two cards of the same type selected
    if (selectedCardTypes.includes(parameterCardType)) {
      selectedCopy = selected.filter(
        (card) => getCardType(card) !== parameterCardType
      );
    }

    selectedCopy.push(e);
    setSelected(selectedCopy);
  };

  const removeSelect = (e: any) => {
    setSelected(selected.filter((s: any) => s !== e));
  };

  const playAbilityCardOnEnemy = async (
    card: AbilityCardIngame,
    enemy: HordeEnemyIngame | WarlordIngame
  ) => {
    try {
      const payload = {};
      const response = await axios.post(
        `/games/${gameId}/ability-cards/${card.id}`,
        payload,
        { headers: { Authorization: "Bearer " + userToken } }
      );
      console.log(response);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const isSpectator = (_user: User | null) =>
    !loggedUser.username ||
    (_user && _user?.lobby?.game?.id !== parseInt(gameId));

  const playersInRenderOrder = (_players: Player[]) => {
    const orderedPlayerList: Player[] = _players.sort(
      (p1, p2) => p1.turnOrder - p2.turnOrder
    );
    /* In case we are someone playing the game and not a spectator, make sure
     * that the first player of the list (the one who will be rendered on the
     * bottom left part) is us
     * */
    if (user && !isSpectator(user)) {
      // Rotate the list until the current player is at the first position,
      // but they still keep the same order (by rotating values in array)
      while (orderedPlayerList[0].user.username !== loggedUser.username) {
        const firstElement = orderedPlayerList.shift(); // removes it form array
        if (firstElement) orderedPlayerList.push(firstElement); // adds it at the end
      }
    }
    return orderedPlayerList;
  };

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      const _game = response.data;
      setGame(_game);
      const sortedPlayers = playersInRenderOrder(_game.players);
      setPlayers(sortedPlayers);
    } catch (error: any) {
      toast.error(error?.message);
      if (error?.status >= 400) history.push(ROUTES.BROWSE_LOBBIES);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await axios.get(`/users/${loggedUser.username}`);
      setUser(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Game " + gameId;
    loggedUser.username && fetchUser();
    // return function cleanup() {
    //   toast.dismiss("Spectator");
    //   // To avoid sending the user to the lobby, that
    //   // would redirect him/her to the game again
    //   history.replace(ROUTES.HOME);
    // };
  }, []);

  useEffect(() => {
    fetchGame();
    if (isSpectator(user)) {
      // if user is spectator, render a toast
      toast("Spectator", {
        position: "top-center",
        duration: Infinity,
        icon: "👁️",
        id: "Spectator",
      });
    }
  }, [user]);

  useEffect(() => {
    const handleSelected = () => {
      const selectedCardTypes = selected.map((card) => getCardType(card));

      if (
        selectedCardTypes.includes("ABILITY") &&
        selectedCardTypes.includes("ENEMY")
      ) {
        const abilityCard = selected.find(
          (card) => getCardType(card) === "ABILITY"
        );
        const enemyCard = selected.find(
          (card) => getCardType(card) === "ENEMY"
        );
        playAbilityCardOnEnemy(abilityCard, enemyCard);
        setSelected([]);
      }
    };
    handleSelected();
  }, [selected]);

  return (
    game && (
      <selectContext.Provider value={{ selected, addSelect, removeSelect }}>
        <div className="flex justify-center items-end">
          <div className="fixed w-32 z-50 bottom-22 transform hover:-translate-y-32 transition duration-200 ease-in-out">
            {!isSpectator(user) && (
              <PlayerHand player={playersInRenderOrder(game.players)[0]} />
            )}
          </div>
          <div className="flex-1 bg-wood bg-repeat-round h-screen px-16 flex flex-col justify-center">
            {/* Top player names */}
            <div className="flex-none flex justify-between items-center p-2 text-white text-3xl">
              <p>{players[3] && players[3].user.username}</p>
              <p>{players[2] && players[2].user.username}</p>
            </div>
            {/* Game board (felt part)*/}
            <div className="flex-1 bg-felt bg-repeat-round rounded-3xl">
              <div className="h-full p-2 grid grid-cols-5 gap-4">
                <div className="row-span-2">
                  {players[2] && (
                    <PlayerZoneVertical player={players[3]} rotation={90} />
                  )}
                  {/* Top left */}
                </div>
                <div className="col-span-3 row-span-2">
                  <CenterZone game={game} />
                </div>
                <div className="row-span-2">
                  {players[3] && (
                    <PlayerZoneVertical
                      player={players[2]}
                      rotation={90}
                      counterclockwise
                    />
                  )}
                  {/* Top right */}
                </div>
                <div className="col-span-2 self-end max-w-xs 2xl:max-w-sm">
                  {players[0] && <PlayerZoneHorizontal player={players[0]} />}
                  {/* Bottom left (My hand) */}
                </div>
                <div className="self-end">{/* Blank space */}</div>
                <div className="col-span-2 self-end justify-self-end max-w-xs 2xl:max-w-sm">
                  {players[1] && (
                    <PlayerZoneHorizontal player={players[1]} reverse />
                  )}
                  {/* Bottom right */}
                </div>
              </div>
            </div>
            {/* Bottom player names */}
            <div className="flex-none flex justify-between items-center p-2 text-white text-3xl">
              <p>{players[0] && players[0].user.username}</p>
              <p>{players[1] && players[1].user.username}</p>
            </div>
          </div>
        </div>
      </selectContext.Provider>
    )
  );
}
