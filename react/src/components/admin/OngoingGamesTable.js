import { Table } from "react-bootstrap";

export default function OngoingGamesTable() {
  const gameList = [
    {
      id: 2,
      name: "Game 2",
      startTime: "2020-04-01T00:00:00Z",
      hasScenes: true,
    },
  ];

  // TODO load games from server
  // const [gameList, setGameList] = useState([]);
  // const fetchGames = async () => {
  //   try {
  //     const response = await axios.get(`games`);
  //     setGameList(response.data);
  //   } catch (error) {
  //     setErrors([...errors, error.message]);
  //   }
  // };

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
        {gameList.map((game, idx) => (
          <tr>
            <th>game.id</th>
            <th>game.name</th>
            <th>game.start_time</th>
            <th>game.has_scenes</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
