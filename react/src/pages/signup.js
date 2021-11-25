import { useContext, useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import errorContext from "../context/error";

/**
 *
 * @author jstockwell
 * @author andrsdt
 */
export default function SignUp() {
  const history = useHistory(); // hook
  const { setUserToken } = useContext(userContext); // hook
  const { errors, setErrors } = useContext(errorContext); // Array of errors

  useEffect(() => {
    document.title = "NTFH - Sign up";
  });

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData(e.target);
      const formDataObj = Object.fromEntries(formData.entries());
      await axios.post("/users/register", formDataObj); // register response
      // we want to auto log in after registering to get the auth token
      const loginResponse = await axios.post("/users/login", formDataObj);
      setUserToken(loginResponse.data.authorization);
      history.push(ROUTES.HOME);
    } catch (error) {
      setErrors([...errors, error.response]);
    }
  };

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Sign up page</h1>
      <p>
        {" "}
        Have an account?
        <Link to={ROUTES.LOGIN}>
          <Button variant="primary">Log In</Button>
        </Link>
      </p>
      <br />
      <Form onSubmit={handleRegister}>
        <Form.Group className="mb-3">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="username"
            required
          />
          <Form.Text className="text-muted">
            Make sure it's creative! Have fun with it
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            name="email"
            required
          />
          <Form.Text className="text-muted">
            We'll never share your email with anyone else, only Facebook, I'm
            sure they can keep a secret.
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Enter password"
            name="password"
            required
          />
        </Form.Group>

        <Button type="submit">Submit</Button>
      </Form>
    </div>
  );
}
