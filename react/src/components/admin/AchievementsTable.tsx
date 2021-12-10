import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link } from "react-router-dom";
import axios from "../../api/axiosConfig";
import * as ROUTES from "../../constants/routes";
import userContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";
import { Achievement } from "../../interfaces/Achievement";

export default function AchievementsTable() {
  const [achievements, setAchievements] = useState<Achievement[]>([]);
  const loggedUser = tokenParser(useContext(userContext));

  useEffect(() => {
    const fetchAchievements = async () => {
      try {
        const response = await axios.get(`achievements`);
        setAchievements(response.data);
      } catch (error: any) {
        toast.error(error.response?.data?.message);
      }
    };

    fetchAchievements();
  }, []);

  const isAdmin = () =>
    loggedUser.username && loggedUser.authorities.includes("admin");

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
                  Achievements
                </th>
                {isAdmin() && (
                  <th scope="col" className="relative px-6 py-3">
                    <span className="sr-only">Edit</span>
                  </th>
                )}
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
                  {isAdmin() && (
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <Link
                        className="text-indigo-300 hover:text-indigo-500"
                        to={ROUTES.EDIT_ACHIEVEMENT.replace(
                          ":achievementId",
                          achievement.id.toString()
                        )}
                      >
                        Edit
                      </Link>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
