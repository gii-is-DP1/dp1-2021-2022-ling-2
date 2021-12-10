import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as IMAGES from "../constants/images";
import * as ROUTES from "../constants/routes";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";
import { User } from "../interfaces/User";

/**
 *
 * @author andrsdt
 */
export default function Home() {
  const { userToken, setUserToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const [user, setUser] = useState<User | null>(null);
  const history = useHistory(); // hook
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
  );

  async function fetchUnregisteredUserData() {
    try {
      const response = await axios.get("/unregistered-users");
      setUnregisteredUser(response.data);
    } catch (error) {}
  }

  async function fetchUserData() {
    try {
      const response = await axios.get(`/users/${loggedUser?.username}`);
      setUser(response.data);
    } catch (error) {}
  }

  const userInLobby = () => !!user?.lobby;

  const userInGame = () => !!user?.lobby?.game;

  const handleLogout = () => {
    setUserToken(null);
    setUser(null);
    toast.success("Logged out successfully");
    history.push(ROUTES.HOME);
  };

  const handleShare = () => {
    const message =
      "Hey, check out this web app for playing No Time for Heroes! " +
      window.location.href;
    // copy to clipboard
    navigator.clipboard.writeText(message);
    // show success message
    toast("Copied to clipboard", { icon: "📋" });
  };

  useEffect(() => {
    document.title = "No Time for Heroes";
  }, []);

  useEffect(() => {
    // TODO extract this to app.tsx? so it runs even if the
    // person without credentials never visits / route
    // Unregistered user creation
    if (!unregisteredUser.username) {
      // if there aren't unregistered user credentials, ask for some
      fetchUnregisteredUserData();
    }
    if (userToken) {
      // if there are user credentials, fetch his/her info
      fetchUserData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="flex flex-row h-screen p-2 bg-wood">
      <div className="flex-none w-1/4 flex flex-col justify-between">
        {/* Left column (statistics, ranking, share)*/}
        <div className="flex flex-col">
          <Link to={ROUTES.STATISTICS}>
            <button type="submit" className="btn-ntfh mb-2">
              <p className="text-gradient-ntfh">Statistics</p>
            </button>
          </Link>
          <Link to="" className="btn-ntfh w-min">
            <p className="text-gradient-ntfh">Ranking</p>
          </Link>
        </div>
        <button className="btn-ntfh w-min" onClick={handleShare}>
          <p className="text-gradient-ntfh">Share!</p>
        </button>
      </div>
      <div className="flex-1 w-1/2 flex flex-col items-center justify-center my-6">
        {/* Center column (logo, browse/join/admin buttons) */}
        <img
          className="flex flex-row xl:w-2/3"
          src={IMAGES.LOGO}
          alt="NTFH logo"
        ></img>
        <div className="flex flex-col justify-center items-center flex-auto gap-y-2">
          {/* Buttons */}
          {loggedUser && !userInLobby() && (
            <Link to={ROUTES.CREATE_LOBBY}>
              <button type="submit" className="btn-ntfh">
                <p className="text-gradient-ntfh">Create Lobby</p>
              </button>
            </Link>
          )}
          {!userInLobby() && (
            <Link to={ROUTES.BROWSE_LOBBIES}>
              <button type="submit" className="btn-ntfh">
                <p className="text-gradient-ntfh">Browse Games</p>
              </button>
            </Link>
          )}
          {user && userInLobby() && !userInGame() && (
            <Link
              to={ROUTES.LOBBY.replace(":lobbyId", user?.lobby.id.toString())}
            >
              <button type="submit" className="btn-ntfh">
                <p className="text-gradient-ntfh">Rejoin Lobby</p>
              </button>
            </Link>
          )}
          {user && userInGame() && (
            <Link
              to={ROUTES.GAME.replace(":gameId", user.lobby.game.id.toString())}
            >
              <button type="submit" className="btn-ntfh">
                <p className="text-gradient-ntfh">Rejoin Game</p>
              </button>
            </Link>
          )}
          {hasAuthority(loggedUser, "admin") && (
            <Link to={ROUTES.ADMIN_PAGE}>
              <button type="submit" className="btn-ntfh">
                <p className="text-gradient-ntfh">Admin Tools</p>
              </button>
            </Link>
          )}
        </div>
      </div>
      <div className="flex-none w-1/4 flex flex-col items-end">
        {/* Left column (profile, friends stuff)*/}
        {loggedUser ? (
          <>
            <Link to={ROUTES.PROFILE.replace(":username", loggedUser.username)}>
              <button className="btn-ntfh mb-2" type="submit">
                <p className="text-gradient-ntfh">{loggedUser.username}</p>
              </button>
            </Link>
            <button className="btn-ntfh" type="submit" onClick={handleLogout}>
              <p className="text-gradient-ntfh">Log out</p>
            </button>
          </>
        ) : (
          <>
            <div className="btn-ntfh mb-4">
              <p className="text-gradient-ntfh">{unregisteredUser?.username}</p>
            </div>
            <Link to={ROUTES.SIGNUP}>
              <button className="btn-ntfh mb-2" type="submit">
                <p className="text-gradient-ntfh">Sign up</p>
              </button>
            </Link>
            <Link to={ROUTES.LOGIN}>
              <button className="btn-ntfh" type="submit">
                <p className="text-gradient-ntfh">Log in</p>
              </button>
            </Link>
          </>
        )}
      </div>
    </div>
  );
}
