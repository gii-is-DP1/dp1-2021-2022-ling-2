import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory } from "react-router";
import UserContext from "../../context/user";
import axios from "../../api/axiosConfig";
import ErrorContext from "../../context/error";

export default function UsersTable() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const { errors, setErrors } = useContext(ErrorContext); // Array of errors
  const [userList, setUserList] = useState([]);

  const fetchUsers = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get(`users`, { headers });
      setUserList(response.data);
    } catch (error) {
      setErrors([...errors, error.response?.data]);
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
            <Button
              variant="primary"
              onClick={() => history.push(`/profile/${user.username}/edit`)}
            >
              Edit profile
            </Button>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
