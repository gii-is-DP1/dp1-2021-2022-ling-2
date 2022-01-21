import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import CharacterStatisticsTable from "../components/statistics/CharacterStatisticsTable";

type UserStatsPOJO = {
  averageDuration?: string;
  charactersPlayed?: {
    [key: string]: number;
  };
  charactersWinRates?: {
    [key: string]: number;
  };
  fastestMatch?: string;
  gloryEarned?: number;
  killCount?: number;
  longestMatch?: string;
  matchesPlayed?: number;
  matchesWon?: number;
};

/**
 *
 * @author andrsdt
 */
export default function UserStatistics() {
  const { username: profileUsername } = useParams<{ username: string }>(); // hook
  const [userStats, setUserStats] = useState<UserStatsPOJO>({});

  useEffect(() => {
    document.title = `NTFH - ${profileUsername}'s statistics`;
    const fetchUserStatistics = async () => {
      try {
        const response = await axios.get(`statistics/users/${profileUsername}`);
        setUserStats(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };
    fetchUserStatistics();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">
              {profileUsername}'s statistics
            </p>
          </button>
        </span>
        <div className="flex flex-row bg-felt rounded-3xl border-20 border-gray-900 p-8 text-2xl divide-x-4 divide-gray-600 space-x-8">
          <div className="flex flex-col justify-center space-y-4">
            <span>Matches played: {userStats.matchesPlayed}</span>
            <span>Matches won: {userStats.matchesWon ?? 0}</span>
            <span>Fastest match: {userStats.fastestMatch ?? "--:--:--"}</span>
            <span>Longest match: {userStats.longestMatch ?? "--:--:--"}</span>
            <span>
              Average match duration: {userStats.averageDuration ?? "--:--:--"}
            </span>
            <span>Total glory earned: {userStats.gloryEarned ?? 0}</span>
            <span>Total kill count: {userStats.killCount ?? 0}</span>
          </div>
          <div className="pl-8">
            <CharacterStatisticsTable
              charactersPlayed={userStats.charactersPlayed}
              charactersWinRates={userStats.charactersWinRates}
            />
          </div>
        </div>
      </div>
    </>
  );
}
