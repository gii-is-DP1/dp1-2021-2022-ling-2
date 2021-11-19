import { createContext } from "react";
import tokenToUser from "../helpers/tokenToUser";
/**
 * @see https://es.reactjs.org/docs/context.html
 */
export default createContext({
  token: localStorage.getItem("token"),
  setToken: (token) => localStorage.setItem("token", token),
});
// this context will be used to know if there is a user logged in
