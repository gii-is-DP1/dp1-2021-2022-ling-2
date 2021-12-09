import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user.ts";
import tokenParser from "../helpers/tokenParser";

/**
 *
 * @author andrsdt
 */
export default function Profile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { userToken } = useContext(userContext);
  const user = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null);
  const [userGamesHistory, setUserGamesHistory] = useState([]);
  const [matchesWon, setMatchesWon] = useState(1402);
  const [fastestMatch, setFastestMatch] = useState("26m54s");
  const [longestMatch, setLongestMatch] = useState("1h48m");

  useEffect(() => {
    const fetchGameHistory = async () => {
      try {
        // TODO remove auth if not needed
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`gameHistory`, { headers });
        const gamesPlayedByUser = filterByUsername(response.data);
        setUserGamesHistory(gamesPlayedByUser);
      } catch (error) {
        toast.error(error.response?.data?.message);
      }
    };

    fetchGameHistory();
  }, []);

  useEffect(() => {
    document.title = `NTFH - ${params.username}'s profile`;

    // get user profile
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(`users/${params.username}`);
        setUserProfile(response.data);
      } catch (error) {
        toast.error(error.response?.data?.message);
        history.push("/not-found");
      }
    };
    fetchUserProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  {
    /* TODO Replace with a backend filter */
  }

  const userInPlayerList = (list, username) => {
    return list.some((player) => player.user.username === username);
  };

  const filterByUsername = (list) =>
    list.filter((gameHistory) =>
      userInPlayerList(gameHistory.game.players, user.username)
    );

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8">
        <span className="text-center pb-8">
          <Link to={ROUTES.PROFILE.replace(":username", params.username)}>
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
            <span>Matches played: {userGamesHistory.length}</span>
            <span>Matches won: {matchesWon}</span>
            <span>Fastest match: {fastestMatch}</span>
            <span>Longest match: {longestMatch}</span>
            <div className="flex flex-wrap space-x-3">
              <Link to={ROUTES.STATISTICS + `/${params.username}`}>
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Stats</p>
                </button>
              </Link>
              <Link
                to={ROUTES.EDIT_PROFILE.replace(":username", params.username)}
              >
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Edit</p>
                </button>
              </Link>
              <Link to={ROUTES.ACHIEVEMENTS}>
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Achievements</p>
                </button>
              </Link>
            </div>
          </div>
          <div className="flex flex-col w-3/5">
            {/* match history table */}
            <GamesHistoryTable data={userGamesHistory} />
          </div>
        </span>
      </div>
    </>
  );
}
