import { useContext, useEffect, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { useHistory } from "react-router";
import Homebar from "../components/home/Homebar";
import UserContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";
import * as ROUTES from "../constants/routes";
import AchievementsTable from "../components/admin/AchievementsTable";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import OngoingGamesTable from "../components/admin/OngoingGamesTable";
import UsersTable from "../components/admin/UsersTable";

export default function AdminPage() {
  const history = useHistory();
  const user = tokenParser(useContext(UserContext));

  const [currentTable, setCurrentTable] = useState("games");

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
          {currentTable === "gamesHistory" && <GamesHistoryTable />}
          {currentTable === "users" && <UsersTable />}
          {currentTable === "achievements" && <AchievementsTable />}
        </Row>
      </Container>
    </span>
  );
}
