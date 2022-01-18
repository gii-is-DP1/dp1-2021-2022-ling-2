import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import UsersInLobby from "../components/lobby/UsersInLobby";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Game } from "../interfaces/Game";
import { Player } from "../interfaces/Player";
import { CharacterGenderEnum } from "../types/CharacterGenderEnum";
import { CharacterTypeEnum } from "../types/CharacterTypeEnum";
import { templateGame } from "../templates/game";
import { templatePlayer } from "../templates/player";
import { threadId } from "worker_threads";
/**
 *
 * @author andrsdt
 */
export default function Lobby() {
  const REFRESH_RATE = 1000; // fetch lobby status every 1000 miliseconds
  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [game, setGame] = useState<Game>(templateGame); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { gameId } = useParams<{ gameId: string }>(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const [player, setPlayer] = useState<Player>(templatePlayer);
  const [character, setCharacter] = useState<CharacterTypeEnum | null>(null);
  const [gender, setGender] = useState<CharacterGenderEnum>("MALE");
  const [fullLobby, setFullLobby] = useState<boolean>(false);
  const [charactersTaken, setCharactersTaken] = useState<CharacterTypeEnum[]>(
    []
  );

  const characters: CharacterTypeEnum[] = [
    "RANGER",
    "ROGUE",
    "WARRIOR",
    "WIZARD",
  ];
  const genders: CharacterGenderEnum[] = ["MALE", "FEMALE"];

  const getCharacterId = () => {
    if (character === null) return null;
    return 1 + 2 * characters.indexOf(character) + genders.indexOf(gender);
    // Input: WARRIOR, FEMALE
    // Output: 3+ 1  = 4 (id Of FEMALE WARRIOR in the DB is 4)
    // This is a temporal solution to be refactored in the future
  };

  const isHost = () => {
    return loggedUser.username === game.leader?.user?.username;
  };

  async function fetchLobbyStatus() {
    try {
      const response = await axios.get(`/games/${gameId}`);
      const lobby: Game = response.data;
      if (game !== templateGame && !userInLobby(loggedUser.username, lobby)) {
        // if I was in the list of the previous lobby and not, I was kicked. Send me to browse lobbies
        toast("You have been kicked from the lobby");
        history.goBack();
        return;
      }
      const state = lobby.hasStarted ? "ONGOING" : "LOBBY";
      const prevState = game.hasStarted ? "ONGOING" : "LOBBY";
      if (game !== templateGame && state !== prevState)
        window.location.reload();

      setGame(lobby);
      setFullLobby(lobby.maxPlayers === lobby.players.length);
      const takenCharacters: CharacterTypeEnum[] = lobby.players.map(
        (_player) => _player?.character?.characterTypeEnum
      );
      setPlayer(
        lobby.players.find(
          (_player) => _player.user.username === loggedUser.username
        ) ?? templatePlayer
      );
      setCharactersTaken(takenCharacters);
      return lobby;
    } catch (error: any) {
      // TODO: Throw NotFoundError on the backend with the message "this lobby does not exist anymore"
      toast.error(error?.message);
      if (error?.status === 404) history.push(ROUTES.BROWSE_GAMES);
      return;
    }
  }

  async function joinLobby() {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.post(
        `/games/${gameId}/add/${loggedUser.username}`,
        null,
        {
          headers,
        }
      );
      setGame(response.data);
    } catch (error: any) {
      toast.error(error?.message);
      if (error?.status === 404) history.push(ROUTES.BROWSE_GAMES);
    }
  }

  async function deleteLobby() {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      await axios.delete(`/games/${gameId}`, { headers });
      history.replace(ROUTES.BROWSE_GAMES);
      toast.success("Lobby deleted successfully");
    } catch (error: any) {
      toast.error(error?.message);
    }
  }

  async function leaveLobby() {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      await axios.post(`/games/${gameId}/remove/${loggedUser.username}`, null, {
        headers,
      });
      history.push(ROUTES.BROWSE_GAMES);
    } catch (error: any) {
      toast.error(error?.message);
    }
  }

  const userInLobby = (_username: string, _game: Game) => {
    return _game.players.some((p) => p.user.username === _username);
  };

  const startGame = async (e: React.MouseEvent) => {
    e.preventDefault();
    try {
      const payload = game;
      await axios.post(`/games/${gameId}/start`, payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Game lobby";
    if (!loggedUser.username) history.push(ROUTES.LOGIN); // missing token
    async function firstFetch() {
      const _lobby = await fetchLobbyStatus();
      // We have to notify the server we have joined the lobby
      // will be only executed if the user is not in the lobby yet
      if (_lobby && !userInLobby(loggedUser.username, _lobby)) joinLobby();
    }
    firstFetch();
  }, []); // Only run once

  useEffect(() => {
    // TODO extract timer to hook
    const interval = setInterval(() => setTime(Date.now()), REFRESH_RATE); // Useful later for fetching lobby users
    return () => {
      clearInterval(interval); // when the component is unmounted, clean up to prevent memory leaks
    };
  }, []); // Update "time" state every second

  useEffect(() => {
    // Fetch the lobby status from the server to update the state and re-render if needed
    fetchLobbyStatus();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [time]); // Since "time" state variable is updated every second, we can use it to fetch the lobby users periodically

  useEffect(() => {
    async function updateUserCharacter() {
      try {
        await axios.put(
          `/players/${player?.id}/character/${getCharacterId()}`,
          null,
          {
            headers: { Authorization: "Bearer " + userToken },
          }
        );
      } catch (error: any) {
        toast.error(error?.message);
      }
    }
    if (character == null) return;
    updateUserCharacter();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [character, gender]);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen items-center p-6 bg-wood bg-repeat">
        <div className="flex-none btn-ntfh text-5xl">
          <p className="text-gradient-ntfh">Lobby</p>
        </div>
        <div className="flex-1 flex flex-col w-3/4 lg:w-2/3 xl:w-1/2 justify-center">
          <div className="flex bg-felt rounded-3xl border-20 border-gray-900 p-8 divide-x-2 divide-gray-900 text-xl">
            <div className="w-3/5">
              <div className="flex flex-col items-start">
                {/* Game name, delete lobby */}
                <div className="flex items-baseline space-x-5">
                  <p className="mb-1">{game.name}</p>

                  <button
                    className="btn-ntfh mb-3"
                    onClick={isHost() ? deleteLobby : leaveLobby}
                  >
                    <p className="text-gradient-ntfh text-2xl">
                      {isHost() ? "Delete " : "Leave "}
                      lobby
                    </p>
                  </button>
                </div>
                <div className="mb-4">
                  {/* Waiting people, counter */}
                  <p>
                    {fullLobby
                      ? "The room is full"
                      : "Waiting for people to join"}
                  </p>
                  <p>
                    Players in the lobby: {game.players.length}/
                    {game.maxPlayers}
                  </p>
                </div>
                {<UsersInLobby game={game} />}
              </div>
              {/* Left col ( game info, current players)*/}
            </div>
            <div className="w-2/5">
              {/* Right col (Choose character and version, start game button) */}
              <form className="flex flex-col px-3 mb-6">
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setCharacter(null)}
                  ></input>
                  <label>🌫️ None</label>
                </span>
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    disabled={
                      character !== "ROGUE" && charactersTaken.includes("ROGUE")
                    }
                    onChange={(e) => setCharacter("ROGUE")}
                  ></input>
                  <label
                    className={
                      character === "ROGUE" || charactersTaken.includes("ROGUE")
                        ? "text-gray-600"
                        : ""
                    }
                  >
                    🗡️ Rogue
                  </label>
                </span>
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    disabled={
                      character !== "WARRIOR" &&
                      charactersTaken.includes("WARRIOR")
                    }
                    onChange={(e) => setCharacter("WARRIOR")}
                  ></input>
                  <label
                    className={
                      character === "WARRIOR" ||
                      charactersTaken.includes("WARRIOR")
                        ? "text-gray-600"
                        : ""
                    }
                  >
                    🛡 Warrior
                  </label>
                </span>
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    disabled={
                      character !== "WIZARD" &&
                      charactersTaken.includes("WIZARD")
                    }
                    onChange={(e) => setCharacter("WIZARD")}
                  ></input>
                  <label
                    className={
                      character === "WIZARD" ||
                      charactersTaken.includes("WIZARD")
                        ? "text-gray-600"
                        : ""
                    }
                  >
                    🧙 Wizard
                  </label>
                </span>
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    disabled={
                      character !== "RANGER" &&
                      charactersTaken.includes("RANGER")
                    }
                    onChange={(e) => setCharacter("RANGER")}
                  ></input>
                  <label
                    className={
                      character === "RANGER" ||
                      charactersTaken.includes("RANGER")
                        ? "text-gray-600"
                        : ""
                    }
                  >
                    🏹 Ranger
                  </label>
                </span>
              </form>
              <form className="flex flex-col px-3">
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setGender("MALE")}
                  ></input>
                  <label>♂ Male</label>
                </span>
                <span>
                  <input
                    className="mr-2"
                    name="class"
                    type="radio"
                    onChange={(e) => setGender("FEMALE")}
                  ></input>
                  <label>♀ Female</label>
                </span>
              </form>

              {isHost() && game.players.length > 1 && (
                <button
                  disabled={game.players.length < 2}
                  className="btn-ntfh ml-2 mt-6"
                  type="submit"
                  onClick={startGame}
                >
                  <p className="text-gradient-ntfh">Start Game</p>
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
