import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from "../../api/axiosConfig";
import playerParser from "../../helpers/playerParser";
import timeParser from "../../helpers/timeParser";
import { Game } from "../../interfaces/Game";

export default function OngoingGamesTable() {
  const [gameList, setGameList] = useState<Game[]>([]);

  useEffect(() => {
    // get lobby list
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games`);
        setGameList(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };

    fetchGames();
  }, []);

  const tableHeaders = ["Id", "Start Time", "Scenes", "Leader", "Players"];

  return (
    <div className="flex flex-col">
      <div className="overflow-x-auto">
        <div className="py-2 align-middle inline-block min-w-full">
          <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-800">
                <tr>
                  {tableHeaders.map((header, index) => (
                    <th key={index} scope="col" className="text-table-th">
                      {header}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="bg-gray-900 divide-y divide-gray-200">
                {gameList.map((game) => (
                  <tr key={game.id}>
                    <td className="text-table-td">{game.id}</td>
                    <td className="text-table-td">
                      {timeParser(game.startTime)}
                    </td>
                    <td className="text-table-td">
                      {game.hasScenes ? "🟢" : "🔴"}
                    </td>
                    <td className="text-table-td">
                      {game.leader.user.username}
                    </td>
                    <td className="text-table-td">
                      {playerParser(game.players)}
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
