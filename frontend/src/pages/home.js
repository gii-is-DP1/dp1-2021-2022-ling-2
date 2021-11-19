import { useEffect } from "react";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import { useLocalStorage } from "../hooks/useLocalStorage";

export default function Home() {
  useEffect(() => {
    document.title = "No Time for Heroes";
  });
  const [user, setUser] = useLocalStorage("user", "");

  return (
    <span className="home">
      <h1>Home</h1>
      <div>{user === "" ? <UnregisteredSidebar /> : <Sidebar />}</div>
    </span>
  );
}
