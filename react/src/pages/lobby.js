import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import Homebar from "../components/home/Homebar";
import UsersInLobby from "../components/lobby/UsersInLobby";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

export default function Lobby() {
  const REFRESH_RATE = 1000; // fetch lobby status every 1000 miliseconds

  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [lobby, setLobby] = useState(null); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { lobbyId } = useParams(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const [character, setCharacter] = useState(null);
  const [gender, setGender] = useState("male");
  const [fullLobby, setFullLobby] = useState(false);
  const [charactersTaken, setCharactersTaken] = useState([]);

  const characters = ["ranger", "rogue", "warrior", "wizard"];
  const genders = ["male", "female"];

  const getCharacterId = () => {
    if (character === null) return null;
    return 1 + 2 * characters.indexOf(character) + genders.indexOf(gender);
    // Input: warrior, female
    // Output: 3+ 1  = 4 (id Of female warrior in the DB is 4)
    // This is a temporal solution to be refactored in the future
  };

  const isHost = () => user?.username === lobby?.host?.username;

  async function fetchLobbyStatus() {
    try {
      const response = await axios.get(`/lobbies/${lobbyId}`);
      const newLobby = response.data;
      if (lobby && !userInLobby(user, newLobby)) {
        // if I was in the list of the previous lobby and not, I was kicked. Send me to browse lobbies
        toast("You have been kicked from the lobby");
        history.goBack();
        return;
      }
      if (lobby && lobby.game) {
        history.push(ROUTES.GAME.replace(":gameId", lobby.game.id));
        return;
      }
      setLobby(newLobby);
      setFullLobby(newLobby.maxPlayers === newLobby.users.length);
      const takenCharacters = newLobby.users.map((_user) =>
        _user.character?.characterTypeEnum?.toLowerCase()
      );
      setCharactersTaken(takenCharacters);
      return newLobby;
    } catch (error) {
      // TODO: Throw NotFoundError on the backend with the message "this lobby does not exist anymore"
      toast.error(error.response?.data?.message);
      history.push(ROUTES.BROWSE_LOBBIES);
      return;
    }
  }

  async function notifyJoinLobby() {
    try {
      const payload = { username: user.username };
      const headers = { Authorization: "Bearer " + userToken };
      await axios.post(`/lobbies/${lobbyId}/join`, payload, {
        headers,
      });
    } catch (error) {
      toast.error(error.response?.data?.message);
      if (error?.response?.status === 404) history.push(ROUTES.BROWSE_LOBBIES);
    }
  }

  async function handleRemoveUserFromLobby(username) {
    try {
      // axios.delete only has 2 parameters, url and headers)
      await axios.delete(`/lobbies/${lobby.id}/remove/${username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
      if (username === lobby.host.username) {
        toast.message("Lobby deleted successfully");
        history.goBack();
      } else if (username === user.username) {
        // If I was the one leaving the lobby
        history.goBack();
      }
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  }

  const userInLobby = (_user, _lobby) =>
    _lobby.users.map((u) => u.username).includes(_user.username);

  const createGame = async (e) => {
    e.preventDefault();
    try {
      const payload = lobby;
      const response = await axios.post("/games", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      const gameId = response.data.gameId;
      history.push(ROUTES.GAME.replace(":gameId", gameId));
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  };

  useEffect(() => {
    // We have to notify the server we have joined the lobby
    document.title = "NTFH - Game lobby";

    async function firstFetch() {
      const _lobby = await fetchLobbyStatus();
      // will be only executed if the user is not in the lobby yet
      if (_lobby && !userInLobby(user, _lobby)) notifyJoinLobby();
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
          username: user.username,
          character: getCharacterId(),
        };
        await axios.put(`/users/character`, payload, {
          headers: { Authorization: "Bearer " + userToken },
        });
      } catch (error) {
        toast.error(error.response?.data?.message);
      }
    }
    updateUserCharacter();
  }, [character, gender]);

  const html = (
    <>
      <Homebar />
      {lobby && (
        <>
          <Row>
            <Col>
              <h1>Lobby - {lobby.name}</h1>
              {/* TODO separate method for deleting game. Currently it's handled in the
               user removal endpoint, it should have its own endpoint and Axios call */}
              <Button onClick={(e) => handleRemoveUserFromLobby(user.username)}>
                {lobby.host.username === user.username ? "Delete" : "Leave"}{" "}
                lobby
              </Button>
              <div>
                {fullLobby ? "The room is full" : "Waiting for people to join"}
              </div>
              <div>
                Players in the lobby: {lobby.users.length}/{lobby.maxPlayers}
              </div>
              <br />
              <UsersInLobby
                lobby={lobby}
                handleRemoveUserFromLobby={handleRemoveUserFromLobby}
              />
              <br />
            </Col>
            <Col>
              <Form>
                <Form.Group key="stacked-radio">
                  <Form.Label>Class</Form.Label> <br />
                  <Form.Check
                    className="mx-3"
                    label="🌫️ None"
                    name="class"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setCharacter(null)}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🗡️ Rogue"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("rogue") || character === "rogue"
                    }
                    onChange={(e) => setCharacter("rogue")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🛡 Warrior"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("warrior") ||
                      character === "warrior"
                    }
                    onChange={(e) => setCharacter("warrior")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🧙 Wizard"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("wizard") ||
                      character === "wizard"
                    }
                    onChange={(e) => setCharacter("wizard")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🏹 Ranger"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("ranger") ||
                      character === "ranger"
                    }
                    onChange={(e) => setCharacter("ranger")}
                  />
                </Form.Group>
                <Form.Group className="mb-1">
                  <Form.Label>Variant</Form.Label> <br />
                  <Form.Check
                    className="mx-3"
                    label="♂ Male"
                    name="gender"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setGender("male")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="♀ Female"
                    name="gender"
                    type="radio"
                    onChange={(e) => setGender("female")}
                  />
                </Form.Group>
              </Form>
            </Col>
          </Row>
          <Row>
            {lobby.users.length > 1 && user.username === lobby.host.username ? (
              <Button type="submit" classType="mx-auto" onClick={createGame}>
                Start Game
              </Button>
            ) : (
              ""
            )}
          </Row>
        </>
      )}
    </>
  );

  if (!lobby) return <></>; // Don't render anything if the lobby is not loadedw

  return (
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
                  onClick={(e) => handleRemoveUserFromLobby(user.username)}
                >
                  <p className="text-gradient-ntfh text-2xl">
                    {lobby.host.username === user.username
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
                  Players in the lobby: {lobby.users.length}/{lobby.maxPlayers}
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
                    character !== "rogue" && charactersTaken.includes("rogue")
                  }
                  onChange={(e) => setCharacter("rogue")}
                ></input>
                <label
                  className={
                    (character === "rogue" ||
                      charactersTaken.includes("rogue")) &&
                    "text-gray-600"
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
                    character !== "warrior" &&
                    charactersTaken.includes("warrior")
                  }
                  onChange={(e) => setCharacter("warrior")}
                ></input>
                <label
                  className={
                    (character === "warrior" ||
                      charactersTaken.includes("warrior")) &&
                    "text-gray-600"
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
                    character !== "wizard" && charactersTaken.includes("wizard")
                  }
                  onChange={(e) => setCharacter("wizard")}
                ></input>
                <label
                  className={
                    (character === "wizard" ||
                      charactersTaken.includes("wizard")) &&
                    "text-gray-600"
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
                    character !== "ranger" && charactersTaken.includes("ranger")
                  }
                  onChange={(e) => setCharacter("ranger")}
                ></input>
                <label
                  className={
                    (character === "ranger" ||
                      charactersTaken.includes("ranger")) &&
                    "text-gray-600"
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
                  onChange={(e) => setGender("male")}
                ></input>
                <label>♂ Male</label>
              </span>
              <span>
                <input
                  className="mr-2"
                  name="class"
                  type="radio"
                  onChange={(e) => setGender("female")}
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
  );
}

Lobby.propTypes = {
  User: PropTypes.shape({
    username: PropTypes.string.isRequired,
  }),

  users: PropTypes.arrayOf(PropTypes.User),
};
