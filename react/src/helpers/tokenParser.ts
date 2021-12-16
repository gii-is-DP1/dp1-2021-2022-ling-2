import jwtDecode from "jwt-decode";
import { TokenUser } from "../interfaces/TokenUser";

/**
 * @author andrsdt
 * @param {String} jwt Token returned by the server
 * @return Object with username, password and authority list
 */
const tokenParser = (context: { userToken: string }): TokenUser => {
  // TODO rename to parseToken
  const token: string = context.userToken;
  if (!token) return { username: "", password: "", authorities: [] };
  const parsedToken: any = jwtDecode(token);
  const data = parsedToken.data;
  data.authorities = parsedToken.authorities;
  return data;
};

export default tokenParser;
