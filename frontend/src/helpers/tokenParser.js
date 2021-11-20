import jwtDecode from "jwt-decode";

/**
 * @author andrsdt
 * @param {String} jwt Token returned by the server
 * @return Object with email, id and
 */
const tokenParser = (context) => {
  // TODO rename to parseToken
  const parsedToken = jwtDecode(context.userToken);
  const data = parsedToken.data;
  data.authorities = parsedToken.authorities;
  return data;
};

export default tokenParser;
