import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Lobby from "../components/game/Lobby";
import OngoingGame from "../components/game/Game";

export default function Game() {
  // route for /games/gameId
  const { gameId } = useParams(); // get params from react router link
  const [hasStarted, setHasStarted] = useState(false);
  const [error, setError] = useState(null);

  // TODO: Change or remove. This was coded prior game-lobby separation
  // useEffect(() => {
  //   async function gameHasStarted(gameId) {
  //     try {
  //       const response = axios.get(`/api/games/${gameId}`);
  //       if (!response.data.startTime) {
  //         // if there isn't a StartTime, the game has
  //         setHasStarted(true);
  //       }
  //     } catch (error) {
  //       setError(error);
  //       console.log(error);
  //     }
  //   }
  //   gameHasStarted(gameId);
  // }, [hasStarted, setHasStarted, gameId]);
  // return (
  //   <>
  //     {error && <p>{error.message}</p>}
  //     {hasStarted ? <OngoingGame /> : <Lobby />}
  //   </>
  // );
}
