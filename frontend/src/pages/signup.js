import { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import * as ROUTES from "../constants/routes";
import Homebar from "../components/home/Homebar";
import { API_BASE_URL } from "../constants/paths";

export default function SignUp() {
  useEffect(() => {
    document.title = "NTFH - Sign up";
  });

  const onFormSubmit = e => {
    e.preventDefault()
    const formData = new FormData(e.target),
    formDataObj = Object.fromEntries(formData.entries())
    console.log(formDataObj)
    // TODO POST of sign up
    // fetch(this.props.formAction, {
    //   headers: {
    //     'Accept': 'application/json',
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(formDataObj)
    // });

    // this.setState({description: ''});
  }

  const [username, setUsername] = useState(document.getElementById("signUpUsername"));
  const [emailAddress, setEmailAddress] = useState(document.getElementById("formBasicEmail"));
  const [password, setPassword] = useState(document.getElementById("formBasicPassword"));
  const [error, setError] = useState("");

  return (
    <div>
      <div><Homebar /></div>
      <h1>Sign up page</h1>
      <p>
        {" "}
        Have an account?
        <Link to={ROUTES.LOGIN}>
          <Button variant="primary">Log In</Button>
        </Link>
      </p>
      <br/>
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
          <Form.Label>Email</Form.Label>
          <Form.Control 
            type="email" 
            placeholder="Enter email" 
            name="email" 
            required isInvalid
          />
          <Form.Text className="text-muted">
          We'll never share your email with anyone else, only Facebook, I'm sure they can keep a secret.
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
