import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory } from "react-router";
import { Link } from "react-router-dom";
import axios from "../api/axiosConfig";
import AchievementsTable from "../components/admin/AchievementsTable";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import OngoingGamesTable from "../components/admin/OngoingGamesTable";
import UsersTable from "../components/admin/UsersTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user.ts";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";

/**
 *
 * @author andrsdt
 */
export default function AdminPage() {
  const history = useHistory();
  const user = tokenParser(useContext(userContext));
  const { userToken } = useContext(userContext);
  const [currentTable, setCurrentTable] = useState("ongoingGames");
  const [gamesHistory, setGamesHistory] = useState([]);

  useEffect(() => {
    const fetchGameHistory = async () => {
      if (currentTable !== "gamesHistory") return;
      try {
        // TODO remove auth if not needed
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.get(`gameHistory`, { headers });
        setGamesHistory(response.data);
      } catch (error) {
        toast.error(error.response?.data?.message);
      }
    };
    fetchGameHistory();
  }, [currentTable]);

  useEffect(() => {
    document.title = "NTFH - Admin panel";
    if (!hasAuthority(user, "admin")) {
      toast.error("You must be an admin to access this page");
      history.push(ROUTES.LOGIN);
    }
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8">
        <span className="text-center pb-8">
          <Link to={ROUTES.ADMIN_PAGE}>
            <button type="submit" className="btn-ntfh">
              <p className="text-5xl text-gradient-ntfh">Admin Tools</p>
            </button>
          </Link>
        </span>
        <span className="flex flex-row flex-1 justify-between">
          <div class="flex flex-col w-1/4 max-w-1/5 justify-center gap-y-4">
            {/* 4 botones de seleccionar tabla */}
            <button
              type="submit"
              className="btn-ntfh"
              onClick={() => setCurrentTable("ongoingGames")}
            >
              <p className="text-gradient-ntfh">Ongoing games</p>
            </button>
            <button
              type="submit"
              className="btn-ntfh"
              onClick={() => setCurrentTable("gamesHistory")}
            >
              <p className="text-gradient-ntfh">Game history</p>
            </button>
            <button
              type="submit"
              className="btn-ntfh"
              onClick={() => setCurrentTable("users")}
            >
              <p className="text-gradient-ntfh">Users</p>
            </button>
            <button
              type="submit"
              className="btn-ntfh"
              onClick={() => setCurrentTable("achievements")}
            >
              <p className="text-gradient-ntfh">Achievements</p>
            </button>
          </div>
          <div class="flex flex-col w-3/4 items-center justify-center pl-12">
            {/* Tabla seleccionada */}
            {currentTable === "ongoingGames" && <OngoingGamesTable />}
            {currentTable === "gamesHistory" && (
              <GamesHistoryTable data={gamesHistory} />
            )}
            {currentTable === "users" && <UsersTable />}
            {currentTable === "achievements" && <AchievementsTable />}
          </div>
        </span>
      </div>
    </>
  );
}
