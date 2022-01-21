import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import { Game } from "../interfaces/Game";
import { User } from "../interfaces/User";

/**
 *
 * @author andrsdt
 */

type UserStatsPOJO = {
  averageDuration?: string;
  charactersPlayed?: object;
  charactersWinRates?: object;
  fastestMatch?: string;
  gloryEarned?: number;
  killCount?: number;
  longestMatch?: string;
  matchesPlayed?: number;
  matchesWon?: number;
};

export default function Profile() {
  const { username: profileUsername } = useParams<{ username: string }>(); // hook
  const history = useHistory(); // hook

  const [userProfile, setUserProfile] = useState<User | null>(null);
  const [userGamesHistory, setUserGamesHistory] = useState<Game[]>([]);
  const [userStats, setUserStats] = useState<UserStatsPOJO>({});

  useEffect(() => {
    const fetchGameHistory = async () => {
      try {
        const response = await axios.get(`users/${profileUsername}/history`);
        setUserGamesHistory(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };

    fetchGameHistory();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    document.title = `NTFH - ${profileUsername}'s profile`;

    // get user profile
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(`users/${profileUsername}`);
        setUserProfile(response.data);
      } catch (error: any) {
        toast.error(error?.message);
        history.push("/not-found");
      }
    };

    const fetchUserStatistics = async () => {
      try {
        const response = await axios.get(`statistics/users/${profileUsername}`);
        setUserStats(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };
    fetchUserProfile();
    fetchUserStatistics();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8">
        <span className="text-center pb-8">
          <Link to={ROUTES.PROFILE.replace(":username", profileUsername)}>
            <button type="submit" className="btn-ntfh">
              <p className="text-5xl text-gradient-ntfh">Profile</p>
            </button>
          </Link>
        </span>
        <span className="flex flex-row justify-around">
          <div className="flex flex-col max-w-1/3 justify-center items-center space-y-4 text-2xl bg-felt border-20 border-gray-900 rounded-3xl m-8 shadow-2xl p-6">
            {/* username, email, matches, fastest and longest matches, stats and edit buttons */}
            <span>Username: {userProfile?.username}</span>
            <span>Email: {userProfile?.email}</span>
            <h1 className="pt-5 font-bold">Statistics</h1>
            <span>Matches played: {userGamesHistory.length}</span>
            <span>Matches won: {userStats.matchesWon ?? 0}</span>
            <span>Fastest match: {userStats.fastestMatch ?? "--:--:--"}</span>
            <span>Longest match: {userStats.longestMatch ?? "--:--:--"}</span>
            <span>
              Average match duration: {userStats.averageDuration ?? "--:--:--"}
            </span>
            <span>Total glory earned: {userStats.gloryEarned ?? 0}</span>
            <span>Total kill count: {userStats.killCount ?? 0}</span>
            {/* <span>Characters played: {userStats.charactersPlayed ?? ""}</span> */}
            {/* <span>
              Characters win rates: {userStats.charactersWinRates ?? ""}
            </span> */}
            <div className="flex flex-wrap space-x-3">
              <Link
                to={ROUTES.EDIT_PROFILE.replace(":username", profileUsername)}
              >
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Edit</p>
                </button>
              </Link>
              <Link
                to={ROUTES.USER_ACHIEVEMENTS.replace(
                  ":username",
                  profileUsername
                )}
              >
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Achievements</p>
                </button>
              </Link>
            </div>
          </div>
          <div className="flex flex-col w-2/3">
            {/* match history table */}
            <GamesHistoryTable />
          </div>
        </span>
      </div>
    </>
  );
}
