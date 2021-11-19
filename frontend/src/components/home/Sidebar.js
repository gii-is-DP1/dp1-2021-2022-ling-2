import { useContext } from "react";
import UserContext from "../../context/user";

/**
 * Sidebar of a registered user. Will contain info about friends, etc.
 * @returns {React.Component}
 */
export default function Sidebar() {
  const { token } = useContext(UserContext);
  return (
    <div>
      <h1>Welcome back, {token}</h1>
    </div>
  );
}
