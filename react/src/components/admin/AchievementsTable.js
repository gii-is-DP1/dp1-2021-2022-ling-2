import { useContext, useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import axios from "../../api/axiosConfig";
import * as ROUTES from "../../constants/routes";
import errorContext from "../../context/error";
import userContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";

export default function AchievementsTable() {
  const { errors, setErrors } = useContext(errorContext);
  const [achievements, setAchievements] = useState([]);
  const user = tokenParser(useContext(userContext));

  useEffect(() => {
    const fetchAchievements = async () => {
      try {
        const response = await axios.get(`achievements`);
        setAchievements(response.data);
      } catch (error) {
        setErrors([...errors, error.response.data]);
      }
    };

    fetchAchievements();
  }, []);

  const isAdmin = (_user) => _user.authorities.includes("admin");

  return (
    <Table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody>
        {achievements.map((achievement, idx) => (
          <tr key={idx}>
            <th>{achievement.name}</th>
            <th>{achievement.description}</th>
            <th>
              <Link
                to={ROUTES.EDIT_ACHIEVEMENT.replace(
                  ":achievementId",
                  achievement.id
                )}
              >
                {isAdmin(user) && <Button type="submit">Edit</Button>}
              </Link>
            </th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
