import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useLocation } from "react-router-dom";
import axios from "../../api/axiosConfig";
import * as ROUTES from "../../constants/routes";
import userContext from "../../context/user";
import { Achievement } from "../../interfaces/Achievement";

type Params = {
  user?: string;
};

export default function AchievementsTable(params: Params) {
  const user = params.user;
  const [achievements, setAchievements] = useState<Achievement[]>([]);
  const { userToken } = useContext(userContext);
  const location = useLocation();
  const [page, setPage] = useState(0);
  const [achievementCount, setAchievementCount] = useState(0);
  const achievementsPerPage = 5;
  const totalPages = Math.floor(achievementCount / achievementsPerPage);

  const isInAdminConsole = () => location.pathname === "/admin";

  const prettifyAchievements = (achievementsInput: Achievement[]) =>
    achievementsInput.forEach(
      (a) =>
        (a.description = a.description.replace(
          "{X}",
          a.condition?.toString() || " — "
        ))
    );

  const fetchUserAchievements = async () => {
    try {
      const response = await axios.get(`/users/${user}/achievements`, {
        params: { page: page, size: achievementsPerPage },
      });
      prettifyAchievements(response.data);
      setAchievements(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchAllAchievements = async () => {
    try {
      const response = await axios.get(`/achievements`, {
        params: { page: page, size: achievementsPerPage },
      });
      prettifyAchievements(response.data);
      setAchievements(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchTotalPages = async () => {
    try {
      const response = await axios.get("achievements/count");
      setAchievementCount(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchUserPages = async () => {
    try {
      const response = await axios.get(`/users/${user}/achievements/count`);
      setAchievementCount(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleSetPage = (amount: number) => {
    const newPage = page + amount;
    // Make sure the page is not out of bounds before updating
    if (totalPages >= newPage && newPage >= 0) setPage(newPage);
  };

  useEffect(() => {
    // Fetch the total number of users only once to set the totalPages variable
    user ? fetchUserPages() : fetchTotalPages();
  }, []);

  useEffect(() => {
    user ? fetchUserAchievements() : fetchAllAchievements();
  }, [page, setPage]);

  const handleDeleteAchievement = async (achievement: Achievement) => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      await axios.delete(`achievements/${achievement.id}`, {
        headers,
      });
      setAchievementCount(achievementCount - 1);
      toast.success("Achievement deleted");
      user ? fetchUserAchievements() : fetchAllAchievements();
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  return (
    <div className="flex flex-col">
      <div className="py-2 align-middle min-w-full">
        <div className="shadow overflow-hidden border-b border-gray-800 sm:rounded-lg">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-800">
              <tr>
                <th
                  scope="col"
                  className="px-10 py-3 text-left text-xs font-medium text-gray-200 uppercase tracking-wider"
                >
                  {isInAdminConsole() ? (
                    <button className="btn-ntfh">
                      <Link
                        to={ROUTES.CREATE_ACHIEVEMENT}
                        className="flex items-center"
                      >
                        <p className="text-gradient-ntfh">New achievement</p>
                      </Link>
                    </button>
                  ) : (
                    "Achievements"
                  )}
                </th>
                <th></th>
                <th
                  scope="col"
                  className="flex justify-end text-table-th space-x-5 text-lg"
                >
                  <p>
                    {page + 1}/{totalPages + 1}
                  </p>
                  <div>
                    <button onClick={() => handleSetPage(-1)}>⬅️</button>
                    <button onClick={() => handleSetPage(+1)}>➡️</button>
                  </div>
                </th>
              </tr>
            </thead>
            <tbody className="bg-gray-900 divide-y divide-gray-600">
              {achievements.map((achievement, idx) => (
                <tr key={idx}>
                  <td className="px-6 py-4 ">
                    <div className="flex items-center">
                      <div className="ml-4">
                        <div className="text-sm font-medium text-white">
                          {achievement.name}
                        </div>
                        <div className="text-sm text-gray-400">
                          {achievement.description}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    {isInAdminConsole() && (
                      <Link
                        className="text-indigo-300 hover:text-indigo-500"
                        to={ROUTES.EDIT_ACHIEVEMENT.replace(
                          ":achievementId",
                          achievement.id.toString()
                        )}
                      >
                        Edit
                      </Link>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    {isInAdminConsole() && (
                      <button
                        className="text-red-300 hover:text-red-500"
                        onClick={() => handleDeleteAchievement(achievement)}
                      >
                        Delete
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
