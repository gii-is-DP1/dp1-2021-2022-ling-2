import { useContext, useEffect, useState } from "react";
import axios from "../api/axiosConfig";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import { useLocalStorage } from "../hooks/useLocalStorage";

export default function Home() {
  const user = useContext(UserContext);
  const [unregisteredUser, setUnregisteredUser] = useLocalStorage(
    "unregisteredUser",
    null
  );
  const [error, setError] = useState("");

  useEffect(() => {
    if (!unregisteredUser) {
      async function fetchData() {
        try {
          const response = await axios.get("/unregistered-users");
          setUnregisteredUser(response.data);
        } catch (error) {
          setError(error);
        }
      }
      fetchData();
    }
  });

  useEffect(() => {
    document.title = "No Time for Heroes";
  });

  return (
    <span className="home">
      <h1>Home</h1>
      {user ? (
        <Sidebar />
      ) : (
        <UnregisteredUserContext.Provider value={unregisteredUser}>
          <UnregisteredSidebar />
        </UnregisteredUserContext.Provider>
      )}
    </span>
  );
}
