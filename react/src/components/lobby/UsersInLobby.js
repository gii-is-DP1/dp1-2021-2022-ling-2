import { ListGroup } from "react-bootstrap";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import Errors from "../common/Errors";
import UserContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";

export default function UsersInLobby(props) {
  const { lobby, handleRemoveUserFromLobby } = props; // destructuring props // TODO in other files too
  // user: the one who is logged in
  const [errors, setErrors] = useState([]);
  const viewer = tokenParser(useContext(UserContext)); // user who is logged in

  const isHost = (user) => user.username === lobby.host;
  // craeate arrow function in a variable

  return (
    <>
      <Errors errors={errors} />
      <ListGroup className="d-inline-flex p-2">
        {lobby.users
          .sort((a, b) =>
            a.username < b.username ? -1 : a.username > b.username ? 1 : 0
          ) // arbitrary but consistent order
          .map((user, idx) => (
            <ListGroup.Item key={idx}>
              {!isHost(user) && isHost(viewer) && (
                // show me the kick button over a player only if i'm the host, and also if the player to kick is not me
                <Button
                  onClick={(e) => handleRemoveUserFromLobby(user.username)}
                >
                  Kick
                </Button>
              )}
              {isHost(user) && "👑"} {user.username}
            </ListGroup.Item>
          ))}
      </ListGroup>
    </>
  );
}
