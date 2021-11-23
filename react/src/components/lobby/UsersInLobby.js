import { ListGroup } from "react-bootstrap";
import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import Errors from "../common/Errors";
import axios from "../../api/axiosConfig";
import UserContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";

export default function UsersInLobby(props) {
  const { lobby } = props; // destructuring props // TODO in other files too
  // user: the one who is logged in
  const [errors, setErrors] = useState([]);
  const { userToken } = useContext(UserContext);
  const viewer = tokenParser(useContext(UserContext)); // user who is logged in

  const isHost = (user) => user.username === lobby.host;
  // craeate arrow function in a variable

  async function handleKickUser(username) {
    try {
      // axios.delete only has 2 parameters, url and headers)
      await axios.delete(`/lobbies/${lobby.id}/remove/${username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
    } catch (error) {
      setErrors([...errors, error.message]);
    }
  }
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
                <Button onClick={(e) => handleKickUser(user.username)}>
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
