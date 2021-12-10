import React, { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";

/**
 *
 * @author jstockwell
 * @author andrsdt
 */
export default function SignUp() {
  const history = useHistory(); // hook
  const { setUserToken } = useContext(userContext); // hook
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSignup = async (e: React.MouseEvent) => {
    e.preventDefault();
    try {
      const payload = {
        username,
        email,
        password,
      };
      await axios.post("/users/register", payload);
      // we want to auto log in after registering to get the auth token
      const loginResponse = await axios.post("/users/login", payload);
      setUserToken(loginResponse.data.authorization);
      toast.success("Successfully registered!");
      history.push(ROUTES.HOME);
    } catch (error: any) {
      toast.error(error.response?.data?.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Sign up";
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen justify-center items-center text-white text-3xl bg-wood p-8">
        <form className="flex flex-col w-2/3 md:w-1/2 xl:w-1/3">
          <div className="flex flex-col mb-6">
            <p className="font-bold text-2xl mb-2">Username</p>
            <input
              type="text"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setUsername(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-6">
            <p className="font-bold text-2xl mb-2">Email</p>
            <input
              type="email"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setEmail(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-6">
            <p className="font-bold text-2xl mb-2">Password</p>
            <input
              type="password"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setPassword(e.target.value)}
            ></input>
          </div>
          <button
            type="submit"
            className="btn-ntfh mb-8"
            onClick={handleSignup}
          >
            <p className="text-gradient-ntfh text-5xl p-2">Sign up</p>
          </button>
          <span className="flex flex-row justify-between items-baseline">
            <span>Already have an account?</span>
            <Link to={ROUTES.LOGIN} className="btn-ntfh w-1/3">
              <p className="text-gradient-ntfh text-3xl text-center">Log in</p>
            </Link>
          </span>
        </form>
      </div>
    </>
  );
}
