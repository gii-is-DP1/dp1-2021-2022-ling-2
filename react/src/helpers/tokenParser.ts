import jwtDecode from "jwt-decode";
import { TokenUser } from "../interfaces/TokenUser";

/**
 * @author andrsdt
 * @param {String} jwt Token returned by the server
 * @return Object with email, id and
 */
const tokenParser = (context: { userToken: string }): TokenUser | null => {
  // TODO rename to parseToken
  const token: string = context.userToken;
  if (token === "" || token === null) return null;
  const parsedToken: any = jwtDecode(token);
  const data = parsedToken.data;
  data.authorities = parsedToken.authorities;
  return data;
};

export default tokenParser;
