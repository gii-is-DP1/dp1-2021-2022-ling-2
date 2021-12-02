import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory } from "react-router";
import axios from "../../api/axiosConfig";
import UserContext from "../../context/user";
import hasAuthority from "../../helpers/hasAuthority";
import tokenParser from "../../helpers/tokenParser";
import toast from "react-hot-toast";

export default function UsersTable() {
  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
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
      toast.success(_user.username + " has been banned");
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  };

  const fetchUsers = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get(`users`, { headers });
      setUserList(response.data);
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  };

  useEffect(() => fetchUsers(), []);

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
