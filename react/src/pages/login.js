import { useContext, useEffect, useState } from "react";
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
export default function Login() {
  const history = useHistory(); // hook
  const { setUserToken } = useContext(userContext); // hook
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        username,
        password,
      };
      const response = await axios.post("/users/login", payload);
      setUserToken(response.data.authorization);
      toast.success("Logged in successfully");
      history.push(ROUTES.HOME);
    } catch (error) {
      toast.error(error.response.data.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Log In";
  });

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen justify-center items-center text-white text-2xl bg-wood p-8">
        <form className="flex flex-col w-2/3 md:w-1/2 xl:w-1/3">
          <div className="flex flex-col mb-6">
            <p className="font-bold text-3xl mb-2">Username</p>
            <input
              type="text"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setUsername(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-6">
            {/* game name and text input field */}
            <p className="font-bold text-3xl mb-2">Password</p>
            <input
              type="password"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setPassword(e.target.value)}
            ></input>
          </div>
          <button type="submit" className="btn-ntfh mb-8" onClick={handleLogin}>
            <p className="text-gradient-ntfh text-6xl p-2">Log in</p>
          </button>
          <span className="flex flex-row justify-between items-baseline">
            <span>Don't have an account?</span>
            <Link to={ROUTES.SIGNUP} className="btn-ntfh w-1/3">
              <p className="text-gradient-ntfh text-2xl text-center">Sign up</p>
            </Link>
          </span>
        </form>
      </div>
    </>
  );
}
