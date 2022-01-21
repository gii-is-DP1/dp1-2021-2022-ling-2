import { useContext } from "react";
import { Link } from "react-router-dom";
import AchievementsTable from "../components/admin/AchievementsTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

/**
 *
 * @author andrsdt
 */
export default function AllAchievements() {
  const loggedUser = tokenParser(useContext(userContext));
  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">All achievements</p>
          </button>
        </span>
        <div className="w-3/4 2xl:w-1/2">
          <AchievementsTable />
          {loggedUser.username && (
            <button className="btn-ntfh">
              <Link
                to={ROUTES.USER_ACHIEVEMENTS.replace(
                  ":username",
                  loggedUser.username
                )}
                className="flex items-center"
              >
                <p className="text-gradient-ntfh">My achievements</p>
              </Link>
            </button>
          )}
        </div>
      </div>
    </>
  );
}
