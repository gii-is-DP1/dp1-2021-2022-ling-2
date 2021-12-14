import { createContext } from "react";
import { UnregisteredUser } from "../interfaces/UnregisteredUser";
/**
 * @see https://es.reactjs.org/docs/context.html
 */

export default createContext<{
  unregisteredUser: UnregisteredUser;
  setUnregisteredUser: any;
}>({
  unregisteredUser: {
    username: "",
    token: -1,
  },
  setUnregisteredUser: null,
});
