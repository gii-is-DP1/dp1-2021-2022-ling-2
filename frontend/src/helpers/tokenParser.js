import jwtDecode from "jwt-decode";

/**
 *
 * @param {String} jwt Token returned by the server
 */
const tokenParser = (context) => {
  const tokenObject = jwtDecode(context.userToken);
  return tokenObject["authorities"][0]["user"];
};

export default tokenParser;
