import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import user from "../context/user";
import userContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

/**
 * @author andrsdt
 */
export default function EditProfile() {
  const params = useParams(); // hook
  const history = useHistory(); // hook

  const { userToken, setUserToken } = useContext(userContext); // hook
  const loggedUser = tokenParser(useContext(userContext)); // hook
  const [userProfile, setUserProfile] = useState(null); // hook

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const sendToProfile = () =>
    history.push(ROUTES.PROFILE.replace(":username", params.username));

  async function fetchUserProfile() {
    try {
      const response = await axios.get(`/users/${params.username}`);
      setUserProfile(response.data);
      setUsername(response.data.username);
      setEmail(response.data.email);
    } catch (error) {
      toast.error(error.response?.data);
      sendToProfile();
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (password !== confirmPassword) {
        toast.error("Passwords do not match");
        return;
      }

      const payload = {
        username,
        email,
      };
      // Only send the pasword if it has been changed
      if (password !== "") payload.password = password;

      const response = await axios.put("/users", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });

      // Issue a new token with the updated user data
      setUserToken(response.data.authorization);
      toast.success("Profile edited successfully");
      sendToProfile();
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  }

  useEffect(() => {
    document.title = `NTFH - Edit profile`;
    if (!userToken) {
      toast.error("You must be logged in to edit your profile");
      history.push(ROUTES.LOGIN);
    }
    // redirect to login if no token
    // redirect to profile if user is not the same as the one in the url or if the user is not an admin
    else if (
      loggedUser.username !== params.username &&
      !loggedUser.authorities.includes("admin")
    ) {
      toast.error("You can't edit another user's profile");
      history.push(ROUTES.PROFILE.replace(":username", params.username));
    } else fetchUserProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <div className="flex flex-col h-screen bg-wood p-8 items-center">
      <span className="text-center pb-8">
        <button type="submit" className="btn-ntfh">
          <p className="text-5xl text-gradient-ntfh">Edit profile</p>
        </button>
      </span>
      <form className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-8">
        <div className="flex flex-col mb-4">
          <p className="font-bold text-2xl mb-2">Username</p>
          <input
            disabled
            value={params.username}
            type="text"
            className="p-3 rounded-xl border-4 border-black text-black"
            onChange={(e) => setUsername(e.target.value)}
          ></input>
        </div>
        <div className="flex flex-col mb-4">
          <p className="font-bold text-2xl mb-2">Email</p>
          <input
            defaultValue={email}
            placeholder="user@mail.com"
            type="email"
            className="p-3 rounded-xl border-4 border-black text-black"
            onChange={(e) => setEmail(e.target.value)}
          ></input>
        </div>
        <div className="flex flex-row justify-between space-x-4">
          <div className="flex flex-col mb-4">
            <p className="font-bold text-2xl mb-2">Password</p>
            <input
              placeholder="********"
              type="password"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setPassword(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-8">
            <p className="font-bold text-2xl mb-2">Confirm Password</p>
            <input
              placeholder="********"
              type="password"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setConfirmPassword(e.target.value)}
            ></input>
          </div>
        </div>
        <button type="submit" className="btn-ntfh" onClick={handleSubmit}>
          <p className="text-gradient-ntfh text-5xl p-2">Edit</p>
        </button>
      </form>
    </div>
  );
}
