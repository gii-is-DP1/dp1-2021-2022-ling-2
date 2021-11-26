import axios from "../../api/axiosConfig";
import { useEffect, useState, useContext } from "react";
import { Table } from "react-bootstrap";
import errorContext from "../../context/error";
import playerParser from "../../helpers/playerParser";
import timeParser from "../../helpers/timeParser";

export default function OngoingGamesTable() {
  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const [gameList, setGameList] = useState([]);

  useEffect(() => {
    // get lobby list
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games`);
        setGameList(response.data);
      } catch (error) {
        setErrors([...errors, error.response?.data]);
      }
    };

    fetchGames();
  }, []);

  return (
    <Table>
      <thead>
        <tr>
          <th>id</th>
          <th>start_time</th>
          <th>has_scenes</th>
          <th>leader</th>
          <th>players</th>
        </tr>
      </thead>
      <tbody>
        {gameList.map((game, idx) => (
          <tr key={idx}>
            <th>{game.id}</th>
            <th>{timeParser(game.startTime)}</th>
            <th>{game.hasScenes ? "🟢" : "🔴"}</th>
            <th>{game.leader.user.username}</th>
            <th>{playerParser(game.players)}</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
