import axios from "../../api/axiosConfig";
import { useEffect, useState } from "react";
import { Table } from "react-bootstrap";

export default function OngoingGamesTable() {
  const [errors, setErrors] = useState([]);
  const [gameList, setGameList] = useState();

  const placeholderGameList = [
    {
      id: 2,
      name: "Game 2",
      startTime: "2020-04-01T00:00:00Z",
      hasScenes: true,
    },
  ];

  useEffect(() => {
    // get lobby list
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games/ongoing`);
        setGameList(response.data);
      } catch (error) {
        setErrors([...errors, error.data.message]);
      }
    };

    fetchGames();
  }, []);

  return (
    <Table>
      <thead>
        <tr>
          <th>id</th>
          <th>name</th>
          <th>start_time</th>
          <th>has_scenes</th>
        </tr>
      </thead>
      <tbody>
        {placeholderGameList.map((game, idx) => (
          <tr>
            <th>{game.id}</th>
            <th>{game.name}</th>
            <th>{game.startTime}</th>
            <th>{game.hasScenes ? "🟢" : "🔴"}</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
