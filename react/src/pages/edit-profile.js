import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Button, Form } from "react-bootstrap";
import * as ROUTES from "../constants/routes";
import Errors from "../components/common/Errors";

/**
 * @author andrsdt
 */
export default function EditProfile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { userToken, setUserToken } = useContext(userContext); // hook
  const user = tokenParser(useContext(userContext)); // hook
  const [errors, setErrors] = useState([]);

  const [username, setUsername] = useState(user.username);
  const [email, setEmail] = useState(user.email);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  useEffect(() => {
    document.title = `NTFH - Edit profile`;
    // TODO allow admin to edit
    if (!userToken) history.push(ROUTES.LOGIN); // redirect to login if no token

    // redirect to profile if user is not the same as the one in the url
    if (user.username !== params.username)
      history.push(ROUTES.PROFILE.replace(":username", params.username));

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (password !== confirmPassword)
        throw new Error("Passwords do not match");

      const payload = {
        username,
        email,
      };
      if (password !== "") payload.password = password;

      const response = await axios.put("/users", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });

      // Issue a new token with the updated user data
      setUserToken(response.data.authorization);
      history.push(ROUTES.PROFILE.replace(":username", params.username));
    } catch (error) {
      setErrors([...errors, error.message]);
    }
  }
  return (
    <>
      <h1>Edit your profile</h1>
      <br />
      <Errors errors={errors} />
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formBasicUsername">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="username"
            defaultValue={user?.username}
            disabled
          />
        </Form.Group>
        <Form.Group controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            name="email"
            defaultValue={user?.email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </Form.Group>
        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Enter password"
            name="password"
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>
        <Form.Group controlId="formBasicPassword">
          <Form.Label>Confirm password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Confirm password"
            name="confirmPassword"
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </>
  );
}
