import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Button } from "react-bootstrap";
import Homebar from "../components/home/Homebar";
import errorContext from "../context/error";

export default function Profile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const user = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null);

  useEffect(() => {
    document.title = `NTFH - ${params.username}'s profile`;

    // get user profile
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(`users/${params.username}`);
        setUserProfile(response.data);
      } catch (error) {
        setErrors([...errors, error.response.data]);
        history.push("/not-found");
      }
    };
    fetchUserProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <>
      <div>
        <Homebar />
      </div>
      <h1>{params.username}'s profile</h1>
      {user?.username === params.username && (
        <Button
          variant="primary"
          onClick={() => history.push(`${params.username}/edit`)}
        >
          Edit profile
        </Button>
      )}
      {userProfile ? (
        <div>
          <p>Username: {userProfile.username}</p>
          <p>Email: {userProfile.email}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </>
  );
}
