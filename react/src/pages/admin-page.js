import axios from "axios";
import { useEffect, useState, useContext } from "react";
import { Button, Col, Container, Row, Table } from "react-bootstrap";
import { useHistory } from "react-router";
import UserContext from "../context/user";
import Homebar from "../components/home/Homebar";

export default function AdminPage() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const [gameList, setGameList] = useState([]);
  const [userList, setUserList] = useState([]);
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    // TODO make these calls when clicking on the corresponding button
    const fetchGames = async () => {
      try {
        const response = await axios.get(`games`);
        setGameList(response.data);
      } catch (error) {
        setErrors([...errors, error.message]);
      }
    };
    // TODO make these calls when clicking on the corresponding button
    const fetchUsers = async () => {
      try {
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`users`, { headers });
        setUserList(response.data);
      } catch (error) {
        setErrors([...errors, error.message]);
      }
    };

    fetchUsers();
  }, []);

  return (
    <div>
      <Homebar />
      <Container>
        <Row>
          {/* Buttons Column */}
          <Col>
            <Row>
              <Button type="submit" onClick={() => ""}>
                Games
              </Button>
            </Row>
            <Row>
              <Button type="submit" onClick={() => ""}>
                History
              </Button>
            </Row>
            <Row>
              <Button type="submit" onClick={() => ""}>
                Users
              </Button>
            </Row>
            <Row>
              <Button type="submit" onClick={() => ""}>
                Achievements
              </Button>
            </Row>
          </Col>

          {/* Data Column */}
          <Col>
            {/* Game Table */}
            <Table>
              <thead>
                <tr>
                  <th>id</th>
                  <th>name</th>
                  <th>duration</th>
                  <th>start_time</th>
                  <th>finish_time</th>
                  <th>has_finished</th>
                  <th>has_scenes</th>
                </tr>
              </thead>
              <tbody>
                {gameList.map((game, idx) => (
                  <tr>
                    <th>game.id</th>
                    <th>game.name</th>
                    <th>game.duration</th>
                    <th>game.start_time</th>
                    <th>game.finish_time</th>
                    <th>game.has_finished</th>
                    <th>game.has_scenes</th>
                  </tr>
                ))}
              </tbody>
            </Table>

            {/* Users Table */}
            <Table>
              <thead>
                <tr>
                  <th>username</th>
                  <th>email</th>
                  <th>enabled</th>
                  <th>modify</th>
                </tr>
              </thead>
              <tbody>
                {userList.map((user, idx) => (
                  <tr>
                    <th>{user.username}</th>
                    <th>{user.email}</th>
                    <th>{user.enabled ? "🟢" : "🔴"}</th>
                    <Button
                      variant="primary"
                      onClick={() =>
                        history.push(`/profile/${user.username}/edit`)
                      }
                    >
                      Edit profile
                    </Button>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>
        </Row>
      </Container>
    </div>
  );
}
