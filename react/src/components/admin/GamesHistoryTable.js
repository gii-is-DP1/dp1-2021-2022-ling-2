import { Table } from "react-bootstrap";

export default function GamesHistoryTable() {
  const gameList = [
    {
      id: 1,
      name: "Game 1",
      duration: "00:45:16",
      startTime: "2020-04-01T00:00:00Z",
      finishTime: "2020-04-01T00:45:16Z",
      hasScenes: true,
    },
  ];

  return (
    <Table>
      <thead>
        <tr>
          <th>id</th>
          <th>name</th>
          <th>duration</th>
          <th>start_time</th>
          <th>finish_time</th>
          <th>has_scenes</th>
        </tr>
      </thead>
      <tbody>
        {gameList.map((game, idx) => (
          <tr>
            <th>game.id</th>
            <th>game.name</th>
            <th>game.duration</th>
            <th>game.start_time</th>
            <th>game.finish_time</th>
            <th>game.has_scenes</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
