import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import Ranking from "../components/ranking/ranking";

type GlobalStats = {
  totalUsers?: number;
  matchesPlayed?: number;
  averageGamesPerUser?: number;
  totalGameHours?: number;
  rankingByWins?: Array<{
    first: string;
    second: number;
  }>;
  rankingByGlory?: Array<{
    first: string;
    second: number;
  }>;
  rankingByKills?: Array<{
    first: string;
    second: number;
  }>;
};

/**
 *
 * @author andrsdt
 */
export default function Statistics() {
  const [globalStats, setGlobalStats] = useState<GlobalStats>({});

  useEffect(() => {
    const fetchGlobalStats = async () => {
      try {
        const response = await axios.get("statistics/global");
        setGlobalStats(response.data);
        console.log(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };
    fetchGlobalStats();
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Global statistics</p>
          </button>
        </span>
        <span className="flex flex-row space-x-3">
          <div className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-8 text-2xl">
            <h2>Total Games Played:</h2>
            <p className="font-bold">{globalStats.matchesPlayed ?? 0}</p>
            <h2 className="mt-4">Total game hours:</h2>
            <p className="font-bold">{globalStats.totalGameHours ?? 0}</p>
            <h2 className="mt-4">Average games per user:</h2>
            <p className="font-bold">
              {(globalStats.averageGamesPerUser ?? 0).toFixed(1)}
            </p>
            <h2 className="mt-4"> Total users: </h2>
            <p className="font-bold">{globalStats.totalUsers ?? 0}</p>
          </div>
          <Ranking
            rankingByWins={globalStats.rankingByWins}
            rankingByGlory={globalStats.rankingByGlory}
            rankingByKills={globalStats.rankingByKills}
          />
        </span>
      </div>
    </>
  );
}
