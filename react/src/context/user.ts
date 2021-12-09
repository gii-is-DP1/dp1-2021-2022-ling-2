import { createContext } from "react";
/**
 * @see https://es.reactjs.org/docs/context.html
 */

type Token = string;
export default createContext<{ userToken: Token; setUserToken: any }>({
  userToken: "",
  setUserToken: null,
});
