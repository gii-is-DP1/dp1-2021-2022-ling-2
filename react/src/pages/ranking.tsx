import axios from "axios";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import HomeButton from "../components/common/home-button";

/**
 *
 * @author andrsdt
 */

type Stats = {
  first: string;
  second: number;
};

export default function Ranking() {
  const [winRanking, setWinRanking] = useState<Stats[]>([]);

  useEffect(() => {
    const fetchRankingByWins = async () => {
      try {
        const response = await axios.get(`/stats/ranking/wins`);
        setWinRanking(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };
    fetchRankingByWins();
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
        <div className="flex justify-evenly w-3/4 bg-felt rounded-3xl border-20 border-gray-900 p-8 divide-x-2 divide-gray-700 text-2xl ">
          <div className="flex flex-col items-center w-full space-y-4">
            <h2 className="text-3xl font-bold">Wins</h2>
            {winRanking.map((stat: Stats) => (
              <span className="flex bg-yellow-100 rounded-md p-2">
                <h1>
                  {stat.first || "username"} | 🏆 {stat.second}
                </h1>
              </span>
            ))}
          </div>
          <div className="flex flex-col items-center w-full">
            <h2 className="text-3xl font-bold">Glory</h2>
          </div>
          <div className="flex flex-col items-center w-full">
            <h2 className="text-3xl font-bold">Kills</h2>
          </div>
        </div>
      </div>
    </>
  );
}
