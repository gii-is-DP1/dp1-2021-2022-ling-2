import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import { templateGame } from "../templates/game";
import { GameStateEnum } from "../types/GameStateEnum";
import Game from "./game";
import GameSummary from "./game-summary";
import Lobby from "./lobby";
import LobbyBrowser from "./lobby-browser";

export default function GameRouter() {
  const history = useHistory();
  const { gameId } = useParams<{ gameId: string }>();
  // TODO route each game page to its own component depending on the state
  const [gameState, setGameState] = useState<GameStateEnum>("LOBBY");

  useEffect(() => {
    const fetchGame = async () => {
      // TODO extract this common function to another file and replace it everywhere
      try {
        const response = await axios.get(`/games/${gameId}`);
        const _game = response.data;
        setGameState(_game.stateType);
      } catch (error: any) {
        toast.error(error?.message);
        if (error?.status >= 400) history.push(ROUTES.BROWSE_GAMES);
      }
    };
    fetchGame();
  }, []);

  switch (gameState) {
    case "LOBBY":
      return <Lobby />;
    case "ONGOING":
      return <Game />;
    case "FINISHED":
      return <GameSummary />;
    default:
      return <LobbyBrowser />;
  }
}
