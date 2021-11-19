import { createContext } from "react";
/**
 * @see https://es.reactjs.org/docs/context.html
 */
export default createContext /*{
  user: {
    user: tokenParser(localStorage.getItem("token")),
    token: localStorage.getItem("token"),
  },
  setUser: (token) => localStorage.setItem("token", token),
}*/();
