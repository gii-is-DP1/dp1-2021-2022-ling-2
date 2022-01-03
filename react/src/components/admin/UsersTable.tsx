import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory } from "react-router";
import axios from "../../api/axiosConfig";
import UserContext from "../../context/user";
import hasAuthority from "../../helpers/hasAuthority";
import tokenParser from "../../helpers/tokenParser";
import { User } from "../../interfaces/User";

export default function UsersTable() {
  const history = useHistory();

  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const [userList, setUserList] = useState<User[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchUsers = async () => {
    try {
      const headers = { Authorization: "Bearer " + userToken };
      const response = await axios.get("users", {
        headers,
        params: { page: page },
      });
      setUserList(response.data);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const fetchTotalPages = async () => {
    try {
      const response = await axios.get("users/count");
      const usersPerPage = 10;
      const userCount = response.data;
      setTotalPages(Math.ceil(userCount / usersPerPage));
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleToggleBan = async (_user: User) => {
    try {
      await axios.put(`/users/${_user.username}/ban`, null, {
        headers: { Authorization: "Bearer " + userToken },
      });
      fetchUsers();
      toast.success(_user.username + " has been banned/unbanned");
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleDeleteUser = async (_user: User) => {
    try {
      await axios.delete(`/users/${_user.username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
      fetchUsers();
      toast.success(_user.username + " has been deleted");
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  const handleSetPage = (amount: number) => {
    const newPage = page + amount;
    // Make sure the page is not out of bounds before updating
    if (totalPages >= newPage && newPage >= 0) setPage(newPage);
  };

  useEffect(() => {
    // Fetch users every time the page changes
    fetchUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, setPage]);

  useEffect(() => {
    // Fetch the total number of users only once to set the totalPages variable
    fetchTotalPages();
  }, []);

  return (
    <div className="flex flex-col">
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
                  <th
                    scope="col"
                    className="flex text-table-th space-x-5 text-lg"
                  >
                    <p>
                      {page + 1}/{totalPages + 1}
                    </p>
                    <div>
                      <button onClick={() => handleSetPage(-1)}>⬅️</button>
                      <button onClick={() => handleSetPage(+1)}>➡️</button>
                    </div>
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
                      <button
                        className="btn btn-blue"
                        onClick={() =>
                          history.push(`/profile/${user.username}/edit`)
                        }
                      >
                        Edit
                      </button>
                      <button
                        className={`btn btn-yellow ${
                          user.username === loggedUser?.username
                            ? "invisible"
                            : ""
                        } `}
                        onClick={() => handleToggleBan(user)}
                      >
                        Ban
                      </button>
                      <button
                        className={`btn btn-red ${
                          user.username === loggedUser?.username
                            ? "invisible"
                            : ""
                        } `}
                        onClick={() => handleDeleteUser(user)}
                      >
                        Delete
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
