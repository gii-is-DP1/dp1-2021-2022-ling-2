import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router";
import axios from "../../api/axiosConfig";
import * as ROUTES from "../../constants/routes";
import UserContext from "../../context/user";
import playerParser from "../../helpers/playerParser";
import { Game } from "../../interfaces/Game";
import { templateGame } from "../../templates/game";

type Props = {
  admin?: boolean;
};

export default function GamesHistoryTable(props: Props) {
  const { admin } = props;
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const [finishedGames, setFinishedGames] = useState<Game[]>([]);
  const { username } = useParams<{ username: string }>();
  const [page, setPage] = useState(0);
  const [finishedGameCount, setfinishedGameCount] = useState(0);
  const gamesPerPage = 10;
  const totalPages = Math.floor(finishedGameCount / gamesPerPage);

  const tableHeaders = [
    "Id",
    "Duration",
    "Start Time",
    "Finish Time",
    "Scenes",
    "Winner",
    "Players",
  ];

  const fetchTotalPages = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get("games/finished/count", { headers });
      setfinishedGameCount(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchUserFinishedGames = async () => {
    try {
      const response = await axios.get(`users/${username}/history`, {
        params: { page: page },
      });
      setFinishedGames(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchAllFinishedGames = async () => {
    try {
      const response = await axios.get(`games/finished`, {
        params: { page: page },
      });
      setFinishedGames(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleDeleteGame = async (game: Game) => {
    try {
      finishedGames.splice(finishedGames.indexOf(game), 1);
      const headers = { Authorization: "Bearer " + userToken };
      await axios.delete(`games/${game.id}`, { headers });
      toast.success(`Game ${game.id} deleted successfully`);
      fetchAllFinishedGames();
      setfinishedGameCount(finishedGameCount - 1);
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
    admin ? fetchAllFinishedGames() : fetchUserFinishedGames();
  }, [page, setPage]);

  useEffect(() => {
    // Fetch the total number of users only once to set the totalPages variable
    fetchTotalPages();
  }, []);

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
                    className="flex text-table-th justify-end space-x-5 text-lg"
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
                {finishedGames.map((game) => (
                  <tr key={game.id}>
                    <td className="text-table-td">{game.id}</td>
                    <td className="text-table-td">{game.duration}</td>
                    <td className="text-table-td">{game?.startTime}</td>
                    <td className="text-table-td">{game?.finishTime}</td>
                    <td className="text-table-td">
                      {game.hasScenes ? "🟢" : "🔴"}
                    </td>
                    <td className="text-table-td">
                      {game?.winner?.user?.username || "deleted"}
                    </td>
                    <td className="text-table-td">
                      {playerParser(game.players)}
                    </td>
                    <td className="p-1 space-x-4">
                      <button
                        className={"btn btn-blue"}
                        onClick={() =>
                          history.push(
                            ROUTES.GAME.replace(":gameId", game.id.toString())
                          )
                        }
                      >
                        Summary
                      </button>
                      {admin && (
                        <button
                          className={"btn btn-red"}
                          onClick={() => handleDeleteGame(game)}
                        >
                          Delete
                        </button>
                      )}
                    </td>
                  </tr>
                ))}{" "}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
