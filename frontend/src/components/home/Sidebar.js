import { useContext } from "react";
import UserContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";
/**
 * Sidebar of a registered user. Will contain info about friends, etc.
 * @returns {React.Component}
 */
export default function Sidebar() {
  const { userToken } = useContext(UserContext);
  return (
    <div>
      <h1>Welcome back, {userToken}</h1>
    </div>
  );
}
