import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory } from "react-router";
import { Link } from "react-router-dom";
import AchievementsTable from "../components/admin/AchievementsTable";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import OngoingGamesTable from "../components/admin/OngoingGamesTable";
import UsersTable from "../components/admin/UsersTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";

type CurrentTableEnum = "ongoing" | "history" | "achievements" | "users";

/**
 *
 * @author andrsdt
 */
export default function AdminPage() {
  const history = useHistory();
  const loggedUser = tokenParser(useContext(userContext));
  const [currentTable, setCurrentTable] = useState<CurrentTableEnum>("ongoing");

  useEffect(() => {
    document.title = "NTFH - Admin panel";
    if (!hasAuthority(loggedUser, "admin")) {
      toast.error("You must be an admin to access this page");
      history.push(ROUTES.LOGIN);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
          <div className="flex flex-col w-1/4 max-w-1/5 justify-center gap-y-4">
            <button
              type="submit"
              className={`btn-ntfh transform-gpu hover:scale-110 focus:scale-110 ${
                currentTable === "ongoing" ? " scale-110" : ""
              }`}
              onClick={() => setCurrentTable("ongoing")}
            >
              <p className="text-gradient-ntfh">Ongoing games</p>
            </button>
            <button
              type="submit"
              className={
                "btn-ntfh transform-gpu hover:scale-110 focus:scale-110"
              }
              onClick={() => setCurrentTable("history")}
            >
              <p className="text-gradient-ntfh">Game history</p>
            </button>
            <button
              type="submit"
              className={
                "btn-ntfh transform-gpu hover:scale-110 focus:scale-110"
              }
              onClick={() => setCurrentTable("users")}
            >
              <p className="text-gradient-ntfh">Users</p>
            </button>
            <button
              type="submit"
              className={
                "btn-ntfh transform-gpu hover:scale-110 focus:scale-110"
              }
              onClick={() => setCurrentTable("achievements")}
            >
              <p className="text-gradient-ntfh">Achievements</p>
            </button>
          </div>
          <div className="flex flex-col w-full items-center justify-start pl-12 pt-12">
            {currentTable === "ongoing" && <OngoingGamesTable />}
            {currentTable === "history" && <GamesHistoryTable admin />}
            {currentTable === "users" && <UsersTable />}
            {currentTable === "achievements" && <AchievementsTable />}
          </div>
        </span>
      </div>
    </>
  );
}
