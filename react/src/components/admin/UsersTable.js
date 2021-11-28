import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory } from "react-router";
import axios from "../../api/axiosConfig";
import popupContext from "../../context/popup";
import UserContext from "../../context/user";
import hasAuthority from "../../helpers/hasAuthority";
import tokenParser from "../../helpers/tokenParser";

export default function UsersTable() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const { popups, setPopups } = useContext(popupContext); // Array of errors
  const [userList, setUserList] = useState([]);

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
      fetchUsers();
      // Add popup message "the user {user} has been banned"
    } catch (error) {
      setPopups([...popups, error.response]);
    }
  };

  const fetchUsers = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get(`users`, { headers });
      setUserList(response.data);
    } catch (error) {
      setPopups([...popups, error.response?.data]);
    }
  };

  useEffect(() => fetchUsers(), []);

  const html = (
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
              {hasAuthority(loggedUser, "admin") && (
                <Button onClick={() => handleToggleBan(_user)}>Ban</Button>
              )}
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

  return (
    <div className="flex flex-col w-full">
      <div className="overflow-x-auto">
        <div className="py-2 align-middle inline-block min-w-full">
          <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-800">
                <tr>
                  <th scope="col" className="text-table-th">
                    Username
                  </th>
                  <th scope="col" className="text-table-th">
                    Email
                  </th>
                  <th scope="col" className="text-table-th">
                    Enabled
                  </th>
                  <th scope="col" className="text-table-th">
                    Modify
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-900 divide-y divide-gray-200">
                {userList.map((user) => (
                  <tr key={user.username}>
                    <td className="text-table-td">{user.username}</td>
                    <td className="text-table-td">{user.email}</td>
                    <td className="text-table-td">
                      {user.enabled ? "🟢" : "🔴"}
                    </td>
                    <td className="space-x-4">
                      {hasAuthority(loggedUser, "admin") && (
                        <button
                          className="btn btn-red"
                          onClick={() => handleToggleBan(user)}
                        >
                          Ban
                        </button>
                      )}
                      <button
                        className="btn btn-blue"
                        onClick={() =>
                          history.push(`/profile/${user.username}/edit`)
                        }
                      >
                        Edit
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
