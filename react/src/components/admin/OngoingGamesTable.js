import { useContext, useEffect, useState } from "react";
import axios from "../../api/axiosConfig";
import popupContext from "../../context/popup";
import playerParser from "../../helpers/playerParser";
import timeParser from "../../helpers/timeParser";

export default function OngoingGamesTable() {
  const { popups, setPopups } = useContext(popupContext); // Array of errors
  const [gameList, setGameList] = useState([]);

  useEffect(() => {
    // get lobby list
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games`);
        setGameList(response.data);
      } catch (error) {
        setPopups([...popups, error.response?.data]);
      }
    };

    fetchGames();
  }, []);

  return (
    <div className="flex flex-col w-full">
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
                    Start time
                  </th>
                  <th scope="col" className="text-table-th">
                    Scenes
                  </th>
                  <th scope="col" className="text-table-th">
                    Leader
                  </th>
                  <th scope="col" className="text-table-th">
                    Players
                  </th>
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
