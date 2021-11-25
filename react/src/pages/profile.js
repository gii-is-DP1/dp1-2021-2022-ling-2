import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Table, Col, Button, Row } from "react-bootstrap";
import Homebar from "../components/home/Homebar";
import errorContext from "../context/error";
import playerParser from "../helpers/playerParser"

export default function Profile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const user = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null);

  const placeholderGameHistory = [
    {
      id: 1,
      game: {
        id: 2,
        startTime: "2020-04-01T00:00:00Z",
        hasScenes: true,
        players: ["stockie", "andres", "admin"],
        leader: ["stockie"],
      },
      finishTime: "2020-04-01T00:45:16Z",
      winner: {
        username: "stockie",
      },
      comments: [],
    },
  ];

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
          </Col>
          <Col>
            <Row>
              <h2>Match history</h2>
            </Row>
            <Row>
              <Table>
                <thead>
                  <tr>
                    <th>duration</th>
                    <th>start_time</th>
                    <th>finish_time</th>
                    <th>has_scenes</th>
                    <th>winner</th>
                    <th>players</th>
                  </tr>
                </thead>
                <tbody>
                  {placeholderGameHistory.map((gameHistory, idx) => (
                    <tr key={idx}>
                      <th>{gameHistory.id}</th>
                      <th>gameHistory.duration</th>
                      <th>{gameHistory.game.startTime}</th>
                      <th>{gameHistory.finishTime}</th>
                      <th>{gameHistory.game.hasScenes ? "🟢" : "🔴"}</th>
                      <th>{gameHistory.winner.username}</th>
                      <th>{playerParser(gameHistory.game.players)}</th>
                    </tr>
                  ))}
                </tbody>
              </Table>
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
