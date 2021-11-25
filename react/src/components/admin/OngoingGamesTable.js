import axios from "../../api/axiosConfig";
import { useEffect, useState, useContext } from "react";
import { Table } from "react-bootstrap";
import errorContext from "../../context/error";
import playerParser from "../../helpers/playerParser";

export default function OngoingGamesTable() {
  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const [gameList, setGameList] = useState();

  const placeholderGameList = [
    {
      id: 2,
      startTime: "2020-04-01T00:00:00Z",
      hasScenes: true,
      players: ["stockie", "andres", "admin"],
      leader: ["stockie"],
    },
    {
      id: 4,
      startTime: "2021-11-13T16:52:01Z",
      hasScenes: false,
      players: ["stockie", "andres"],
      leader: ["andres"],
    },
  ];

  useEffect(() => {
    // get lobby list
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games`);
        setGameList(response.data);
      } catch (error) {
        setErrors([...errors, error.response]);
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
        {placeholderGameList.map((game, idx) => (
          <tr key={idx}>
            <th>{game.id}</th>
            <th>{game.startTime}</th>
            <th>{game.hasScenes ? "🟢" : "🔴"}</th>
            <th>{game.leader}</th>
            <th>{playerParser(game.players)}</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
