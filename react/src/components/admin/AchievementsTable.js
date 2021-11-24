import errorContext from "../../context/error";
import { useContext, useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import axios from "../../api/axiosConfig";

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
    }

    fetchAchievements();
    console.log(achievements[0].name);
  }, []);

  return (
    <Table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {achievements.map((achievement, idx) => {
          <tr>
            <th>{achievement.name}</th>
            <th>{achievement.description}</th>
            <th>
              <Button type="submit" onClick={""}>Edit</Button>
            </th>
          </tr>
        })}
      </tbody>
    </Table>
  );
}
