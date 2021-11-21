import { ListGroup } from "react-bootstrap";
import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import Errors from "../common/Errors";
import axios from "../../api/axiosConfig";

export default function UsersInLobby(props) {
  const { lobby, user } = props; // destructuring props // TODO in other files too
  const isHost = user.id === lobby.hostId;
  const [errors, setErrors] = useState([]);

  async function handleKickUser(username) {
    try {
      await axios.post(`/lobbies/${lobby.id}/kick`, { username });
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
              {!isHost && (
                <Button
                  className="m-2"
                  onClick={(user) => handleKickUser(user.username)}
                >
                  Kick
                </Button>
              )}
              {isHost && "👑"} {user.username}
            </ListGroup.Item>
          ))}
      </ListGroup>
    </>
  );
}
