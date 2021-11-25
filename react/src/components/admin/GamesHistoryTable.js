import { useContext, useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import UserContext from "../../context/user";
import axios from "../../api/axiosConfig";
import errorContext from "../../context/error";
import playerParser from "../../helpers/playerParser";

export default function GamesHistoryTable() {
  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const [gamesHistory, setGameHistory] = useState([]);
  const { userToken } = useContext(UserContext);

  const placeholderGameHistory = [
    {
      id: 1,
      game: {
        id: 2,
        startTime: "2020-04-01T00:00:00Z",
        hasScenes: true,
        players: ["stockie", "andres", "admin"],
        leader: ["stockie"],
      },
      finishTime: "2020-04-01T00:45:16Z",
      winner: {
        username: "stockie",
      },
      comments: [],
    },
    {
      id: 2,
      game: {
        id: 3,
        startTime: "2020-04-01T00:00:00Z",
        hasScenes: true,
        players: ["stockie", "andres"],
        leader: ["stockie"],
      },
      finishTime: "2020-04-01T00:45:16Z",
      winner: {
        username: "stockie",
      },
      comments: [],
    },
  ];

  useEffect(() => {
    const fetchGameHistory = async () => {
      try {
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`gameHistory`, { headers });
        setGameHistory(response.data);
      } catch (error) {
        setErrors([...errors, error.response.data]);
      }
    };

    fetchGameHistory();
  }, []);

  return (
    <Table>
      <thead>
        <tr>
          <th>id</th>
          <th>duration</th>
          <th>start_time</th>
          <th>finish_time</th>
          <th>has_scenes</th>
          <th>winner</th>
          <th>players</th>
        </tr>
      </thead>
      <tbody>
        {placeholderGameHistory.map((gameHistory, idx) => (
          <tr key={idx}>
            <th>{gameHistory.id}</th>
            <th>gameHistory.duration</th>
            <th>{gameHistory.game.startTime}</th>
            <th>{gameHistory.finishTime}</th>
            <th>{gameHistory.game.hasScenes ? "🟢" : "🔴"}</th>
            <th>{gameHistory.winner.username}</th>
            <th>{playerParser(gameHistory.game.players)}</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
