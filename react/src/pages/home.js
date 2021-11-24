import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import axios from "../api/axiosConfig";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import * as ROUTES from "../constants/routes";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";
import ErrorContext from "../context/error";

// import "../resources/css/nord.css";

export default function Home() {
  const { errors, setErrors } = useContext(ErrorContext); // hook
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const [currentUser, setCurrentUser] = useState();
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
  );

  const isAdmin = (_user) => _user.authorities.includes("admin");

  useEffect(() => {
    document.title = "No Time for Heroes";
  });

  useEffect(() => {
    // // User check, getting user from sql
    // if(user) {
    //   async function getCurrentUser() {
    //     try {
    //       const response = await axios.get(`users/${user.username}`);
    //       setCurrentUser(response.data);
    //     } catch (error) {
    //       setErrors([...errors, error.message]);
    //     }
    //   }
    //   getCurrentUser();
    // } 

    // Unregistered user creation
    if(!unregisteredUser) {
      // if there aren't unregistered user credentials, ask for some
      async function fetchData() {
        try {
          const response = await axios.get("/unregistered-users");
          setUnregisteredUser(response.data);
        } catch (error) {
          setErrors([...errors, error.response.data]);
        }
      }
      fetchData();
    }
  },[unregisteredUser, setUnregisteredUser]);

  const generateUserButtons = async () => {
    // TODO create user in lobby logic
    async function getCurrentUser() {
      try {
        const response = await axios.get(`users/${user.username}`);
        setCurrentUser(response.data);
      } catch (error) {
        setErrors([...errors, error.message]);
      }
    }
    console.log(currentUser);
    if (currentUser.lobby===null) {
      return (
        <>
          <Link to={ROUTES.CREATE_LOBBY}>
            <Button type="submit">Create Game</Button>
          </Link>
          <Link to={ROUTES.BROWSE_LOBBIES}>
            <Button type="submit">Browse Games</Button>
          </Link>
        </>
      );
    } else {
      <Button type="submit">Rejoin Game</Button>
    }
  };

  return (
    <span>
      <h1>Home</h1>
      <Errors errors={errors} />

      {/* TODO Rework all buttons, ask James what the fuck he means */}

      {userToken ? <Sidebar /> : <UnregisteredSidebar />}

      {/* Buttons */}
      {!userToken ? (
        <Link to={ROUTES.BROWSE_LOBBIES}>
          <Button type="submit">Browse Games</Button>
        </Link>) : generateUserButtons()
      }

      {/* Admin Page*/}
      {
        user && hasAuthority(user, "admin") ? (
          <Link to={ROUTES.ADMIN_PAGE}>
            <Button type="submit">Admin Page</Button>
      {userToken ? (
        <>
          <Sidebar />
          <Link to={ROUTES.CREATE_LOBBY}>
            <Button type="submit">Create game</Button>
          </Link>
        ) : (
          ""
        )
      }
    </span >
  );
}
