import jwtDecode from "jwt-decode";

/**
 *
 * @param {String} jwt Token returned by the server
 */
const tokenParser = (context) => {
  return jwtDecode(context.userToken)["authorities"][0]["user"];
};

export default tokenParser;
