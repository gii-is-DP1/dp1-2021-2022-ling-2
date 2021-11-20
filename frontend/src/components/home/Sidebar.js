import { useContext } from "react";
import UserContext from "../../context/user";
import { Button } from "react-bootstrap";
import tokenParser from "../../helpers/tokenParser";
/**
 * Sidebar of a registered user. Will contain info about friends, etc.
 * @returns {React.Component}
 */
export default function Sidebar() {
  const { setUserToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  return (
    <div>
      <h1>Welcome back, {user.username}</h1>
      <Button type="submit" onClick={() => setUserToken(null)}>
        Log out
      </Button>
    </div>
  );
}
