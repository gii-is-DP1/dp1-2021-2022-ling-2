import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import tokenParser from "../helpers/tokenParser";
import userContext from "../context/user";
import { Button, Form } from "react-bootstrap";
import * as ROUTES from "../constants/routes";
import errorContext from "../context/error";
import Homebar from "../components/home/Homebar";

export default function EditAchievement() {
  const params = useParams();
  const history = useHistory();
  const { errors, setErrors } = useContext(errorContext);
  const { userToken, setUserToken } = useContext(userContext); // hook
  const loggedUser = tokenParser(useContext(userContext)); // hook

  const [achievement, setAchievement] = useState(null);
  const [name, setName] = useState(null);
  const [description, setDescription] = useState(null);

  const sendToAdminPage = () => history.push(ROUTES.ADMIN_PAGE);

  async function fetchAchievement() {
    try {
      const response = await axios.get(`/achievements/${params.achievementId}`);
      // TODO refactor: can be destructured in one line
      setAchievement(response.data);
      setName(response.data.name);
      setDescription(response.data.description);
    } catch (error) {
      setErrors([...errors, error.response]);
      sendToAdminPage();
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const payload = {
        id: achievement.id, // Needed to identify the achievement
        name: name,
        description: description,
      };
      const response = await axios.put("/achievements", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      sendToAdminPage();
    } catch (error) {
      setErrors([...errors, error.response.data]);
    }
  }

  useEffect(() => {
    document.title = `NTFH - Edit achievement`;

    if (!loggedUser.authorities.includes("admin")) {
      history.push(ROUTES.HOME);
    } else fetchAchievement();
  }, []);

  return (
    <>
      <Homebar />
      <h1>Edit achievement</h1>
      <br />
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formBasicAchievementName">
          <Form.Label>Achievement Name</Form.Label>
          <Form.Control
            type="text"
            placeholder="Achievement Name"
            name="achievementName"
            defaultValue={achievement?.name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </Form.Group>
        <Form.Group controlId="formBasicAchievementDescription">
          <Form.Label>Achievement Description</Form.Label>
          <Form.Control
            type="text"
            placeholder="Achievement Description"
            name="achievementDescription"
            defaultValue={achievement?.description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </>
  );
}
