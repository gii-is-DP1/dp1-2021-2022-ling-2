import { useContext, useEffect, useState } from "react";
import { Button, Col, Row, Table } from "react-bootstrap";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import Homebar from "../components/home/Homebar";
import errorContext from "../context/error";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import * as ROUTES from "../constants/routes";

export default function Profile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const { userToken } = useContext(userContext);
  const user = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null);
  const [gamesHistory, setGamesHistory] = useState([]);

  useEffect(() => {
    const fetchGameHistory = async () => {
      try {
        // TODO remove auth if not needed
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`gameHistory`, { headers });

        setGamesHistory(response.data);
      } catch (error) {
        setErrors([...errors, error.response.data]);
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
        setErrors([...errors, error.response.data]);
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
              <h2>Games played: {filterByUsername(gamesHistory).length}</h2>
            </Row>
            <Row>
              <GamesHistoryTable data={filterByUsername(gamesHistory)} />
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
}
