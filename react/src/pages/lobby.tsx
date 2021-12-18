import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import UsersInLobby from "../components/lobby/UsersInLobby";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Lobby as ILobby } from "../interfaces/Lobby";
import { CharacterGenderEnum } from "../types/CharacterGenderEnum";
import { CharacterTypeEnum } from "../types/CharacterTypeEnum";

/**
 *
 * @author andrsdt
 */
export default function Lobby() {
  const REFRESH_RATE = 1000; // fetch lobby status every 1000 miliseconds

  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [lobby, setLobby] = useState<ILobby | null>(null); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { lobbyId } = useParams<{ lobbyId: string }>(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
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

  const isHost = () => loggedUser.username === lobby?.host?.username;

  async function fetchLobbyStatus() {
    try {
      const response = await axios.get(`/lobbies/${lobbyId}`);
      const newLobby: ILobby = response.data;
      if (lobby && !userInLobby(loggedUser.username, newLobby)) {
        // if I was in the list of the previous lobby and not, I was kicked. Send me to browse lobbies
        toast("You have been kicked from the lobby");
        history.goBack();
        return;
      }
      if (lobby && lobby.game) {
        history.push(ROUTES.GAME.replace(":gameId", lobby.game.id.toString()));
        return;
      }
      setLobby(newLobby);
      setFullLobby(newLobby.maxPlayers === newLobby.users.length);
      const takenCharacters: CharacterTypeEnum[] = newLobby.users.map(
        (_user) => _user.character?.characterTypeEnum
      );
      setCharactersTaken(takenCharacters);
      return newLobby;
    } catch (error: any) {
      // TODO: Throw NotFoundError on the backend with the message "this lobby does not exist anymore"
      toast.error(error?.message);
      if (error?.status === 404) history.push(ROUTES.BROWSE_LOBBIES);
      return;
    }
  }

  async function notifyJoinLobby() {
    try {
      const payload = { username: loggedUser.username };
      const headers = { Authorization: "Bearer " + userToken };
      await axios.post(`/lobbies/${lobbyId}/join`, payload, {
        headers,
      });
    } catch (error: any) {
      toast.error(error?.message);
      if (error?.status === 404) history.push(ROUTES.BROWSE_LOBBIES);
    }
  }

  async function handleRemoveUserFromLobby(username: string) {
    try {
      if (!lobby || !loggedUser.username) return;
      // axios.delete only has 2 parameters, url and headers)
      await axios.delete(`/lobbies/${lobby.id}/remove/${username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
      if (username === lobby.host.username) {
        toast.success("Lobby deleted successfully");
        history.goBack();
      } else if (username === loggedUser.username) {
        // If I was the one leaving the lobby
        history.goBack();
      }
    } catch (error: any) {
      toast.error(error?.message);
    }
  }

  const userInLobby = (_username: string, _lobby: ILobby) =>
    _lobby.users.some((u) => u.username === _username);

  const createGame = async (e: React.MouseEvent) => {
    e.preventDefault();
    try {
      const payload = lobby;
      const response = await axios.post("/games", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      const gameId = response.data.gameId;
      history.push(ROUTES.GAME.replace(":gameId", gameId));
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
      if (_lobby && !userInLobby(loggedUser.username, _lobby))
        notifyJoinLobby();
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
        const payload = {
          username: loggedUser.username,
          character: getCharacterId(),
        };
        await axios.put(`/users/character`, payload, {
          headers: { Authorization: "Bearer " + userToken },
        });
      } catch (error: any) {
        toast.error(error?.message);
      }
    }
    updateUserCharacter();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [character, gender]);

  if (!lobby) return <></>; // Don't render anything if the lobby is not loadedw

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
                  <p className="mb-1">{lobby.name}</p>
                  <button
                    className="btn-ntfh mb-3"
                    onClick={(e) =>
                      handleRemoveUserFromLobby(loggedUser.username)
                    }
                  >
                    <p className="text-gradient-ntfh text-2xl">
                      {lobby.host.username === loggedUser.username
                        ? "Delete "
                        : "Leave "}
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
                    Players in the lobby: {lobby.users.length}/
                    {lobby.maxPlayers}
                  </p>
                </div>
                <UsersInLobby
                  lobby={lobby}
                  handleRemoveUserFromLobby={handleRemoveUserFromLobby}
                />
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

              {isHost() && (
                <button
                  disabled={lobby.users.length < 2}
                  className="btn-ntfh ml-2 mt-6"
                  type="submit"
                  onClick={createGame}
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
