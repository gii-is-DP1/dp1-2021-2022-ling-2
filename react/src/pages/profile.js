import { useContext, useEffect, useState } from "react";
import { Button, Col, Row, Table } from "react-bootstrap";
import toast from "react-hot-toast";
import { Link, useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

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
        toast.error(error.response?.data);
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
        toast.error(error.response?.data);
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

  const html = (
    <>
      <div>
        <Homebar />
      </div>
      <h1>{params.username}'s profile</h1>
      <Table borderless>
        <Row>
          <Col>
            {userProfile ? (
              <div>
                <p>Username: {userProfile.username}</p>
                <p>Email: {userProfile.email}</p>
              </div>
            ) : (
              <p>Loading...</p>
            )}
            <Button
              variant="warning"
              onClick={() => history.push(ROUTES.ACHIEVEMENTS)}
            >
              All achievements
            </Button>
          </Col>
          <Col>
            <Row>
              <h2>Games played: {userGamesHistory.length}</h2>
            </Row>
            <Row>
              <GamesHistoryTable data={userGamesHistory} />
            </Row>
          </Col>
        </Row>
      </Table>
      <div className="d-grid gap-2">
        {user?.username === params.username && (
          <Button
            variant="primary"
            onClick={() => history.push(`${params.username}/edit`)}
          >
            Edit profile
          </Button>
        )}
      </div>
    </>
  );

  return (
    <div className="flex flex-col h-screen bg-wood p-8">
      <span className="text-center pb-8">
        <Link to={ROUTES.PROFILE.replace(":username", params.username)}>
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Profile</p>
          </button>
        </Link>
      </span>
      <span className="flex-1 flex flex-row justify-between">
        <div className="flex flex-col w-2/5 justify-center items-center space-y-4 text-white text-2xl">
          {/* username, email, matches, fastest and longest matches, stats and edit buttons */}
          <span>Username: {userProfile?.username}</span>
          <span>Email: {userProfile?.email}</span>
          <span>Matches played: {userGamesHistory.length}</span>
          <span>Matches won: {matchesWon}</span>
          <span>Fastest match: {fastestMatch}</span>
          <span>Longest match: {longestMatch}</span>
          <div className="space-x-4">
            <Link to={ROUTES.STATISTICS + `/${params.username}`}>
              <button type="submit" className="btn-ntfh">
                <p className="text-3xl text-gradient-ntfh">Stats</p>
              </button>
            </Link>
            <Link
              to={ROUTES.EDIT_PROFILE.replace(":username", params.username)}
            >
              <button type="submit" className="btn-ntfh">
                <p className="text-3xl text-gradient-ntfh">Edit</p>
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
  );
}
