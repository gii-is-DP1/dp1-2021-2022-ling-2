import { useHistory, useParams } from "react-router-dom";
import Game from "./game";
import GameSummary from "./game-summary";
import Lobby from "./lobby";
import * as ROUTES from "../constants/routes";
import { useEffect, useState } from "react";
import axios from "../api/axiosConfig";
import toast from "react-hot-toast";

export default function GameRouter() {
  const history = useHistory();
  const { gameId } = useParams<{ gameId: string }>();
  // TODO route each game page to its own component depending on the state
  const [game, setGame] = useState(undefined);

  useEffect(() => {
    const fetchGame = async () => {
      // TODO extract this common function to another file and replace it everywhere
      try {
        const response = await axios.get(`/games/${gameId}`);
        const _game = response.data;
        setGame(_game);
      } catch (error: any) {
        toast.error(error?.message);
        if (error?.status >= 400) history.push(ROUTES.BROWSE_LOBBIES);
      }
    };
    fetchGame();
  }, []);

  // ! get state dynamically from game
  // ! State
  const state: string = "LOBBY";

  switch (state) {
    case "LOBBY":
      return <Lobby />;
    case "ONGOING":
      return <Game />;
    case "FINISHED":
      return <GameSummary />;
    default:
      return <></>;
  }
}
