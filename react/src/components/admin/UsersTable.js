import { useContext, useEffect, useState } from "react";
import { Button, Table, Alert } from "react-bootstrap";
import { useHistory } from "react-router";
import axios from "../../api/axiosConfig";
import ErrorContext from "../../context/error";
import UserContext from "../../context/user";
import hasAuthority from "../../helpers/hasAuthority";

export default function UsersTable() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const { errors, setErrors } = useContext(ErrorContext); // Array of errors
  const [userList, setUserList] = useState([]);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [enabled, setIsEnabled] = useState(null);

  const renderBan = (user) => {
    var status = "";
    if (user.enabled) status = "Ban";
    else status = "Unban";

    return (
      <Button onClick={changeBanStatus(user)}>{status}</Button>
    );
  }

  const changeBanStatus = async (user) => {
    setUsername(user.username);
    setEmail(user.email);
    setIsEnabled(!user.enabled);
    try {
      const payload = {
        username,
        email,
        enabled
      };
      const response = await axios.put("/users", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      // Add popup message "the user {user} has been banned"
      return (
        <Alert variant="danger">
          The user {username} has been banned
        </Alert>
      );
    } catch (error) {
      setErrors([...errors, error.response]);
    }
  }

  const fetchUsers = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get(`users`, { headers });
      setUserList(response.data);
    } catch (error) {
      setErrors([...errors, error.response]);
    }
  };

  useEffect(() => fetchUsers(), []);

  return (
    <Table>
      <thead>
        <tr>
          <th>username</th>
          <th>email</th>
          <th>enabled</th>
          <th>modify</th>
        </tr>
      </thead>
      <tbody>
        {userList.map((user, idx) => (
          <tr key={idx}>
            <th>{user.username}</th>
            <th>{user.email}</th>
            <th>{user.enabled ? "🟢" : "🔴"}</th>
            <th>
              {renderBan(user)}
              &nbsp;
              <Button
                variant="primary"
                onClick={() => history.push(`/profile/${user.username}/edit`)}>
                Edit profile
              </Button>
            </th>

          </tr>
        ))}
      </tbody>
    </Table>
  );
}
