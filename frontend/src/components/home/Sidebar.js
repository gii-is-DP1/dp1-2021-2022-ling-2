import { useLocalStorage } from "../../hooks/useLocalStorage";

export default function Sidebar() {
  // Only shown to logged users

  const [user, setUser] = useLocalStorage("user", "");
  return (
    <div>
      <h1>Welcome back, {user.username}</h1>
    </div>
  );
}
