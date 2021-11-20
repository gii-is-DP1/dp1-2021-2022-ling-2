import { createContext } from "react";

/**
 * @see https://es.reactjs.org/docs/context.html
 */
export default createContext();
/*{
  unregisteredUser: JSON.stringify(localStorage.getItem("unregisteredUser")),
  setUnregisteredUser: (unregisteredUser) =>
    localStorage.setItem("unregisteredUser", JSON.parse(unregisteredUser)),
}*/
