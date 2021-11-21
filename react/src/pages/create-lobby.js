import { useContext, useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import Homebar from "../components/home/Homebar";
import Errors from "../components/common/Errors";

export default function CreateLobby() {
  const history = useHistory(); // hook

  const { userToken } = useContext(userContext); // hook
  const user = tokenParser(useContext(userContext)); // hook

  const [gameName, setGameName] = useState(`${user.username}'s game`);
  const [maxPlayers, setMaxPlayers] = useState(4);
  const [scenesChecked, setScenesChecked] = useState(false);
  const [spectatorsChecked, setSpectatorsChecked] = useState(false);
  const [errors, setErrors] = useState([]);

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
      setErrors([...errors, error.message]);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Create new lobby";
    if (!userToken) history.push(ROUTES.LOGIN); // Send the user to login screen if not logged in
  });

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Create a new game</h1>
      <Errors errors={errors} />
      <Form onSubmit={handleCreateLobby}>
        <Form.Group className="mb-2">
          <Form.Label>Game name</Form.Label>
          <Form.Control
            type="text"
            placeholder={gameName}
            defaultValue={gameName}
            name="name"
            required
            onChange={(e) => setGameName(e.target.value)}
          />
        </Form.Group>
        <Form.Group key="inline-radio" className="mb-2">
          <Form.Label>Max players</Form.Label>
          <Form.Check
            inline
            label="2"
            name="maxPlayers"
            type="radio"
            onChange={(e) => setMaxPlayers(2)}
          />
          <Form.Check
            inline
            label="3"
            name="maxPlayers"
            type="radio"
            onChange={(e) => setMaxPlayers(3)}
          />
          <Form.Check
            inline
            label="4"
            name="maxPlayers"
            type="radio"
            defaultChecked
            onChange={(e) => setMaxPlayers(4)}
          />
        </Form.Group>
        <Form.Group className="mb-1">
          <Form.Check
            name="scenes"
            type="checkbox"
            label="Scenes"
            onChange={(e) => setSpectatorsChecked(!spectatorsChecked)}
          />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Check
            name="spectatorsAllowed"
            type="checkbox"
            label="Spectators"
            onChange={(e) => setSpectatorsChecked(!spectatorsChecked)}
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          Create lobby
        </Button>
      </Form>
    </div>
  );
}
