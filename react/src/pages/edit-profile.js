import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Button, Form } from "react-bootstrap";
import * as ROUTES from "../constants/routes";
import errorContext from "../context/error";
import Homebar from "../components/home/Homebar";

/**
 * @author andrsdt
 */
export default function EditProfile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { errors, setErrors } = useContext(errorContext); // hook
  const { userToken, setUserToken } = useContext(userContext); // hook
  const loggedUser = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null); // hook

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const sendToProfile = () =>
    history.push(ROUTES.PROFILE.replace(":username", params.username));

  async function fetchUserProfile() {
    try {
      const response = await axios.get(`/users/${params.username}`);
      setUserProfile(response.data);
      setUsername(response.data.username);
      setEmail(response.data.email);
    } catch (error) {
      setErrors([...errors, error.response?.data]);
      sendToProfile();
    }
  }

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
      sendToProfile();
    } catch (error) {
      setErrors([...errors, error.response?.data]);
    }
  }

  useEffect(() => {
    document.title = `NTFH - Edit profile`;
    if (!userToken) history.push(ROUTES.LOGIN);
    // redirect to login if no token
    // redirect to profile if user is not the same as the one in the url or if the user is not an admin
    else if (
      loggedUser.username !== params.username &&
      !loggedUser.authorities.includes("admin")
    )
      history.push(ROUTES.PROFILE.replace(":username", params.username));
    else fetchUserProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <>
      <Homebar />
      <h1>Edit your profile</h1>
      <br />
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formBasicUsername">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="username"
            defaultValue={userProfile?.username}
            disabled
          />
        </Form.Group>
        <Form.Group controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            name="email"
            defaultValue={userProfile?.email}
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
