import { Table } from "react-bootstrap";
import playerParser from "../../helpers/playerParser";
import timeParser from "../../helpers/timeParser";

export default function GamesHistoryTable(props) {
  const { data } = props;

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
        {data.map((gameHistory, idx) => (
          <tr key={idx}>
            <th>{gameHistory?.id}</th>
            <th>{gameHistory?.duration}</th>
            <th>{timeParser(gameHistory?.game?.startTime)}</th>
            <th>{timeParser(gameHistory?.finishTime)}</th>
            <th>{gameHistory?.game?.hasScenes ? "🟢" : "🔴"}</th>
            <th>{gameHistory?.winner?.username}</th>
            <th>{playerParser(gameHistory?.game?.players)}</th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
