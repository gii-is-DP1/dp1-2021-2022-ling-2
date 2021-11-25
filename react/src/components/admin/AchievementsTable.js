import { useContext, useEffect, useState } from "react";
import { Table,Button } from "react-bootstrap";
import axios from "../../api/axiosConfig";
import errorContext from "../../context/error";

export default function AchievementsTable() {
  const { errors, setErrors } = useContext(errorContext);
  const [achievements, setAchievements] = useState([]);

  useEffect(() => {
    const fetchAchievements = async () => {
      try {
        const response = await axios.get(`achievements`);
        setAchievements(response.data);
      } catch (error) {
        setErrors([...errors, error.response]);
      }
    };

    fetchAchievements();
  }, []);

  const editAchievement = (id) => {
    return null;
  };

  
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
            <tr>
              <th>
                {achievement.name}
              </th>
              <th>{achievement.description}</th>
              <th>
                <Button type="submit" onClick={() => editAchievement(achievement.id)}>Edit</Button>
              </th>
            </tr>
          ))}
      </tbody>
    </Table>
  );
}
