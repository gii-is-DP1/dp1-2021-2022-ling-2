import axios from "../api/axiosConfig";
import { useEffect, useState, useContext } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";

/**
 *
 * @author jstockwell
 */
export default function Login() {
  const history = useHistory(); // hook
  const { setUserToken } = useContext(userContext); // hook

  const [error, setError] = useState("");
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData(e.target);
      const formDataObj = Object.fromEntries(formData.entries());
      const response = await axios.post("/users/login", formDataObj);
      setUserToken(response.data.authorization);
      history.push(ROUTES.HOME);
    } catch (error) {
      console.log(error);
      setError(error.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Log In";
  });

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Log in page</h1>
      <p>
        Don't have an account?
        <Link to={ROUTES.SIGNUP}>
          <Button variant="primary">Sign Up</Button>
        </Link>
      </p>
      <br></br>
      {error && <p className="mb-4 text-xs text-primary">{error}</p>}
      <Form onSubmit={handleLogin}>
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
