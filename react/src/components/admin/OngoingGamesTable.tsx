import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from "../../api/axiosConfig";
import UserContext from "../../context/user";
import playerParser from "../../helpers/playerParser";
import { Game } from "../../interfaces/Game";

export default function OngoingGamesTable() {
  const { userToken } = useContext(UserContext);
  const [gameList, setGameList] = useState<Game[]>([]);
  const [page, setPage] = useState(0);
  const [ongoingGameCount, setOngoingGameCount] = useState(0);
  const gamesPerPage = 10;
  const totalPages = Math.floor(ongoingGameCount / gamesPerPage);

  const fetchGamesOngoing = async () => {
    try {
      const response = await axios.get("games/ongoing", {
        params: { page: page },
      });
      setGameList(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchTotalPages = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get("games/ongoing/count", { headers });
      setOngoingGameCount(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleDeleteGame = async (game: Game) => {
    try {
      gameList.splice(gameList.indexOf(game), 1);
      const headers = { Authorization: "Bearer " + userToken };
      await axios.delete(`games/${game.id}`, { headers });
      fetchGamesOngoing();
      setOngoingGameCount(ongoingGameCount - 1);
      toast.success(`Game ${game.id} deleted successfully`);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleSetPage = (amount: number) => {
    const newPage = page + amount;
    // Make sure the page is not out of bounds before updating
    if (totalPages >= newPage && newPage >= 0) setPage(newPage);
  };

  useEffect(() => {
    // Fetch users every time the page changes
    fetchGamesOngoing();
  }, [page, setPage]);

  useEffect(() => {
    // Fetch the total number of users only once to set the totalPages variable
    fetchTotalPages();
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
                  <th
                    scope="col"
                    className="flex text-table-th space-x-5 text-lg"
                  >
                    <p>
                      {page + 1}/{totalPages + 1}
                    </p>
                    <div>
                      <button onClick={() => handleSetPage(-1)}>⬅️</button>
                      <button onClick={() => handleSetPage(+1)}>➡️</button>
                    </div>
                  </th>
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
                    <td className="p-1 space-x-4 flex flex-row justify-end items-center pt-2">
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
