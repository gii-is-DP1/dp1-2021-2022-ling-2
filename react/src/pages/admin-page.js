import { useContext, useEffect, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { useHistory } from "react-router";
import axios from "../api/axiosConfig";
import AchievementsTable from "../components/admin/AchievementsTable";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import OngoingGamesTable from "../components/admin/OngoingGamesTable";
import UsersTable from "../components/admin/UsersTable";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import errorContext from "../context/error";
import userContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";

export default function AdminPage() {
  const history = useHistory();
  const user = tokenParser(useContext(userContext));
  const { userToken } = useContext(userContext);
  const [currentTable, setCurrentTable] = useState("games");
  const [gamesHistory, setGamesHistory] = useState([]);
  const { errors, setErrors } = useContext(errorContext);

  useEffect(() => {
    const fetchGameHistory = async () => {
      if (currentTable !== "gamesHistory") return;
      try {
        // TODO remove auth if not needed
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`gameHistory`, { headers });

        setGamesHistory(response.data);
      } catch (error) {
        setErrors([...errors, error.response?.data]);
      }
    };
    fetchGameHistory();
  }, [currentTable]);

  useEffect(() => {
    document.title = "NTFH - Admin panel";
    if (!hasAuthority(user, "admin")) history.push(ROUTES.LOGIN);
  }, []);

  return (
    <span>
      <Homebar />

      <Container>
        <Row>
          {/* Buttons Column */}
          <Col>
            <Button
              type="submit"
              onClick={() => setCurrentTable("ongoingGames")}
            >
              Ongoing games
            </Button>
          </Col>
          <Col>
            <Button
              type="submit"
              onClick={() => setCurrentTable("gamesHistory")}
            >
              Game history
            </Button>
          </Col>
          <Col>
            <Button type="submit" onClick={() => setCurrentTable("users")}>
              Users
            </Button>
          </Col>
          <Col>
            <Button
              type="submit"
              onClick={() => setCurrentTable("achievements")}
            >
              Achievements
            </Button>
          </Col>
        </Row>
        <Row>
          {currentTable === "ongoingGames" && <OngoingGamesTable />}
          {currentTable === "gamesHistory" && (
            <GamesHistoryTable data={gamesHistory} />
          )}
          {currentTable === "users" && <UsersTable />}
          {currentTable === "achievements" && <AchievementsTable />}
        </Row>
      </Container>
    </span>
  );
}
