import { useContext, useEffect, useState } from "react";
import axios from "../api/axiosConfig";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";

export default function Home() {
  const { token } = useContext(UserContext);
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
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
      {token ? <Sidebar /> : <UnregisteredSidebar />}
    </span>
  );
}
