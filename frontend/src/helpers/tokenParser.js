import jwtDecode from "jwt-decode";

/**
 *
 * @param {String} jwt Token returned by the server
 */
const tokenParser = (jwt) => {
  // return { user: jwtDecode(jwt)["authorities"][0]["user"], token: jwt };
  return jwtDecode(jwt)["authorities"][0]["user"];
};

export default tokenParser;
