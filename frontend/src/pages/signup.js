import axios from "../api/axiosConfig";
import { useEffect, useState, useContext } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import * as ROUTES from "../constants/routes";
import Homebar from "../components/home/Homebar";
import userContext from "../context/user";

/**
 *
 * @author jstockwell
 * @author andrsdt
 */
export default function SignUp() {
  const history = useHistory(); // hook
  const { setToken } = useContext(userContext); // hook

  useEffect(() => {
    document.title = "NTFH - Sign up";
  });

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData(e.target);
      const formDataObj = Object.fromEntries(formData.entries());
      const response = await axios.post("/users/register", formDataObj);
      setToken(response.data.authorization);
      history.push(ROUTES.HOME);
    } catch (error) {
      setError(error.message);
    }
  };

  const [error, setError] = useState("");

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
      {error && <p className="mb-4 text-xs text-primary">{error}</p>}
      <Form onSubmit={handleRegister}>
        <Form.Group className="mb-3">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="username"
            required
            isInvalid
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
            isInvalid
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
            isInvalid
          />
        </Form.Group>

        <Button type="submit">Submit</Button>
      </Form>
    </div>
  );
}
