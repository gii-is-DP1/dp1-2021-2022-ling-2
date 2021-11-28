import { useContext, useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import popupContext from "../context/popup";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

export default function CreateLobby() {
  const history = useHistory(); // hook

  const { popups, setPopups } = useContext(popupContext); // Array of errors
  const { userToken } = useContext(userContext); // hook
  const user = tokenParser(useContext(userContext)); // hook

  const [gameName, setGameName] = useState(`${user.username}'s game`);
  const [maxPlayers, setMaxPlayers] = useState(4);
  const [scenesChecked, setScenesChecked] = useState(false);
  const [spectatorsChecked, setSpectatorsChecked] = useState(false);

  const handleCreateLobby = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        name: gameName,
        maxPlayers: maxPlayers,
        hasScenes: scenesChecked,
        spectatorsAllowed: spectatorsChecked,
        host: user.username,
        players: [user.username],
      };
      const response = await axios.post("/lobbies", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      const lobbyId = response.data.lobbyId;
      history.push(ROUTES.LOBBY.replace(":lobbyId", lobbyId));
    } catch (error) {
      setPopups([...popups, error.response?.data]);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Create new lobby";
    if (!userToken) history.push(ROUTES.SIGNUP); // Send the user to signup screen if not logged in
  });

  return (
    <div className="flex flex-col h-screen items-center p-6 bg-wood bg-repeat">
      <div className="flex-none btn-ntfh text-5xl">
        <p className="text-gradient-ntfh">Create game</p>
      </div>
      <div className="bg-felt w-2/3 lg:w-1/2 2xl:w-1/3 my-16 rounded-3xl border-20 border-gray-900 p-8">
        <div className="flex flex-col items-center space-y-8">
          <div className="flex flex-col w-full">
            {/* game name and text input field */}
            <p className="font-bold text-3xl text-center mb-2">Game name</p>
            <input
              className="p-3 rounded-xl border-4 border-black"
              defaultValue={gameName}
              placeholder={gameName}
              onChange={(e) => setGameName(e.target.value)}
            ></input>
          </div>
          <span className="flex flex-row items-baseline space-x-4">
            {/* number of players and selector of 3 */}
            <p className={"font-bold text-xl"}>Max players</p>
            <button
              className={`btn-radio-ntfh ${
                maxPlayers === 2 && "bg-green-600 text-white"
              }`}
              onClick={() => setMaxPlayers(2)}
            >
              2
            </button>

            <button
              className={`btn-radio-ntfh ${
                maxPlayers === 3 && "bg-green-600 text-white"
              }`}
              onClick={() => setMaxPlayers(3)}
            >
              3
            </button>
            <button
              className={`btn-radio-ntfh ${
                maxPlayers === 4 && "bg-green-600 text-white"
              }`}
              onClick={() => setMaxPlayers(4)}
            >
              4
            </button>
          </span>
          <span className="flex flex-row items-baseline space-x-4">
            {/* scenes and checkbox */}
            <p className="font-bold text-xl">Scenes</p>
            <button
              id="checkScenes"
              className={"btn-checkbox-ntfh"}
              onClick={(e) => setScenesChecked(!scenesChecked)}
            >
              {scenesChecked ? "✔️" : "❌"}
            </button>
          </span>
          <span className="flex flex-row items-baseline space-x-4">
            {/* spectators and checkbox */}
            <p className="font-bold text-xl">Spectators</p>
            <button
              id="checkSpectators"
              className="btn-checkbox-ntfh"
              onClick={(e) => setSpectatorsChecked(!spectatorsChecked)}
            >
              {spectatorsChecked ? "✔️" : "❌"}
            </button>
          </span>
          {/* start game button */}
          <button className="btn-ntfh" onClick={handleCreateLobby}>
            <p className="text-gradient-ntfh">Create lobby</p>
          </button>
        </div>
      </div>
    </div>
  );
}
