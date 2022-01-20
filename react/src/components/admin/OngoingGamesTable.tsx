import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from "../../api/axiosConfig";
import UserContext from "../../context/user";
import playerParser from "../../helpers/playerParser";
import { Game } from "../../interfaces/Game";

export default function OngoingGamesTable() {
  const [gameList, setGameList] = useState<Game[]>([]);
  const { userToken } = useContext(UserContext);

  const fetchGamesOngoing = async () => {
    try {
      const response = await axios.get(`games/ongoing`);
      setGameList(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };
  useEffect(() => {
    fetchGamesOngoing();
  }, []);

  const handleDeleteGame = async (game: Game) => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      await axios.delete(`games/${game.id}`, { headers });
      toast.success("Game deleted successfully");
      fetchGamesOngoing();
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const tableHeaders = ["Id", "Start Time", "Scenes", "Leader", "Players", ""];

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
                    <td className="text-table-td">{game.startTime}</td>
                    <td className="text-table-td">
                      {game.hasScenes ? "🟢" : "🔴"}
                    </td>
                    <td className="text-table-td">
                      {game.leader.user?.username || "deleted"}
                    </td>
                    <td className="text-table-td">
                      {playerParser(game.players)}
                    </td>
                    <td className="space-x-4">
                      <button
                        className={"btn btn-red"}
                        onClick={() => handleDeleteGame(game)}
                      >
                        Delete
                      </button>
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
