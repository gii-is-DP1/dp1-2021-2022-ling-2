import playerParser from "../../helpers/playerParser";
import timeParser from "../../helpers/timeParser";

export default function GamesHistoryTable(props) {
  const { data } = props;

  return (
    <div className="flex flex-col">
      <div className="overflow-x-auto">
        <div className="py-2 align-middle inline-block min-w-full">
          <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-800">
                <tr>
                  <th scope="col" className="text-table-th">
                    Id
                  </th>
                  <th scope="col" className="text-table-th">
                    Duration
                  </th>
                  <th scope="col" className="text-table-th">
                    Start time
                  </th>
                  <th scope="col" className="text-table-th">
                    Finish time
                  </th>
                  <th scope="col" className="text-table-th">
                    Scenes
                  </th>
                  <th scope="col" className="text-table-th">
                    Winner
                  </th>
                  <th scope="col" className="text-table-th">
                    Players
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-900 divide-y divide-gray-200">
                {data.map((gameHistory) => (
                  <tr key={gameHistory.id}>
                    <td className="text-table-td">{gameHistory.id}</td>
                    <td className="text-table-td">{gameHistory.duration}</td>
                    <td className="text-table-td">
                      {timeParser(gameHistory.game.startTime)}
                    </td>
                    <td className="text-table-td">
                      {timeParser(gameHistory.finishTime)}
                    </td>
                    <td className="text-table-td">
                      {gameHistory.game.hasScenes ? "🟢" : "🔴"}
                    </td>
                    <td className="text-table-td">
                      {gameHistory.winner.user.username}
                    </td>
                    <td className="text-table-td">
                      {playerParser(gameHistory.game.players)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
