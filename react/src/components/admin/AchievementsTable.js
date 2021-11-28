import { useContext, useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import axios from "../../api/axiosConfig";
import * as ROUTES from "../../constants/routes";
import popupContext from "../../context/popup";
import userContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";
import hasAuthority from "../../helpers/hasAuthority";

export default function AchievementsTable() {
  const { popups, setPopups } = useContext(popupContext);
  const [achievements, setAchievements] = useState([]);
  const user = tokenParser(useContext(userContext));

  useEffect(() => {
    const fetchAchievements = async () => {
      try {
        const response = await axios.get(`achievements`);
        setAchievements(response.data);
      } catch (error) {
        setPopups([...popups, error.response?.data]);
      }
    };

    fetchAchievements();
  }, []);

  return (
    <div className="flex flex-col">
      <div className="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
        <div className="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
          <div className="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th
                    scope="col"
                    className="px-10 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Achievements
                  </th>
                  <th scope="col" className="relative px-6 py-3">
                    <span className="sr-only">Edit</span>
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {achievements.map((achievement, idx) => (
                  <tr key={idx}>
                    <td className="px-6 py-4 ">
                      <div className="flex items-center">
                        <div className="ml-4">
                          <div className="text-sm font-medium text-gray-900">
                            {achievement.name}
                          </div>
                          <div className="text-sm text-gray-500">
                            {achievement.description}
                          </div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <Link
                        className="text-indigo-600 hover:text-indigo-900"
                        to={ROUTES.EDIT_ACHIEVEMENT.replace(
                          ":achievementId",
                          achievement.id
                        )}
                      >
                        Edit
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
