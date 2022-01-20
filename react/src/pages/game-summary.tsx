import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import { Game } from "../interfaces/Game";
import { Player } from "../interfaces/Player";
import { templateGame } from "../templates/game";

export default function GameSummary() {
  // TODO don't send the game itself but statistics about the game
  const [game, setGame] = useState<Game>(templateGame);
  const { gameId } = useParams<{ gameId: string }>();
  const history = useHistory();

  const rankingEmojis = ["🥇", "🥈", "🥉", "😔"];

  useEffect(() => {
    const fetchGame = async () => {
      // TODO extract this common function to another file and replace it everywhere

      try {
        const response = await axios.get(`/games/${gameId}`);
        const _game = response.data;
        setGame(_game);
      } catch (error: any) {
        toast.error(error?.message);
        if (error?.status >= 400) history.push(ROUTES.BROWSE_GAMES);
      }
    };
    fetchGame();
  }, []);

  const rankingSort = (p1: Player, p2: Player): number => {
    // Sorting criteria for deciding the winner: Sort by glory. If equal, sort by kills.
    const compareGlory = p1.glory - p2.glory;
    return compareGlory === 0 ? p1.kills - p2.kills : compareGlory;
  };

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Game summary</p>
          </button>
        </span>
        <div className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-16 justify-between text-center">
          <div className="overflow-x-auto">
            <div className="py-2 align-middle inline-block min-w-full">
              <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-800">
                    <tr>
                      <th scope="col" className="text-table-th">
                        Rank
                      </th>
                      <th scope="col" className="text-table-th">
                        Character
                      </th>
                      <th scope="col" className="text-table-th">
                        Player
                      </th>
                      <th scope="col" className="text-table-th">
                        Alive
                      </th>
                      <th scope="col" className="text-table-th">
                        Glory
                      </th>
                      <th scope="col" className="text-table-th">
                        Gold
                      </th>
                      <th scope="col" className="text-table-th">
                        Kills
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-gray-900 divide-y divide-gray-200">
                    {game?.players?.sort(rankingSort).map((p, i: number) => (
                      <tr key={p.id}>
                        <td className="text-table-td">{rankingEmojis[i]}</td>
                        <td className="text-table-td">{p.characterTypeEnum}</td>
                        <td className="text-table-td">{p.user?.username}</td>
                        <td className="text-table-td">
                          {p.dead ? "😀" : "🖖"}
                        </td>
                        <td className="text-table-td">{p.glory}</td>
                        <td className="text-table-td">{p.gold}</td>
                        <td className="text-table-td">{p.kills}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
