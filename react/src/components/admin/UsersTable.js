import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory } from "react-router";
import axios from "../../api/axiosConfig";
import ErrorContext from "../../context/error";
import UserContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";

export default function UsersTable() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const { errors, setErrors } = useContext(ErrorContext); // Array of errors
  const [userList, setUserList] = useState([]);

  const isAdmin = (_user) => _user.authorities.includes("admin");

  const handleToggleBan = async (_user) => {
    const payload = {
      username: _user.username,
      email: _user.email,
      enabled: !_user.enabled,
    };
    try {
      await axios.put("/users", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      // Add popup message "the user {user} has been banned"
    } catch (error) {
      setErrors([...errors, error.response]);
    }
  };

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
        {userList.map((_user, idx) => (
          <tr key={idx}>
            <th>{_user.username}</th>
            <th>{_user.email}</th>
            <th>{_user.enabled ? "🟢" : "🔴"}</th>
            <th>
              {isAdmin(loggedUser) && (
                <Button onClick={() => handleToggleBan(_user)}>Ban</Button>
              )}
              &nbsp;
              <Button
                variant="primary"
                onClick={() => history.push(`/profile/${_user.username}/edit`)}
              >
                Edit profile
              </Button>
            </th>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
