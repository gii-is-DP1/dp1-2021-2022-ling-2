import { useState, useContext, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import * as ROUTES from "../constants/routes";
import Homebar from "../components/home/Homebar";
import { API_BASE_URL } from "../constants/paths";


export default function Login() {
  const history = useHistory(); // hook

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const onFormSubmit = e => {
    e.preventDefault()
    const formData = new FormData(e.target),
    formDataObj = Object.fromEntries(formData.entries())
    console.log(formDataObj)
  }

  const [error, setError] = useState("");
  const handleLogin = async (event) => {
    event.preventDefault();

    try {
      history.push(ROUTES.HOME);
    } catch (error) {
      setUsername("");
      setPassword("");
      setError(error.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Log In";
  });

  return (
    <div>
      <div><Homebar /></div>
      <h1>Sign up page</h1>
      <p>
        {" "}
        Don't have an account?
        <Link to={ROUTES.SIGNUP}>
          <Button variant="primary">Sign Up</Button>
        </Link>
      </p>
      <br></br>
      <Form onSubmit={onFormSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Username</Form.Label>
          <Form.Control 
            type="text" 
            placeholder="Enter username" 
            name="username" 
            required isInvalid
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
            required isInvalid
          />
        </Form.Group>

        <Button type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
}
