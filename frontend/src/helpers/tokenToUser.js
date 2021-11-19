import jwtDecode from "jwt-decode";

/**
 *
 * @param {String} jwt Token returned by the server
 */
const tokenToUser = (jwt) => jwtDecode(jwt)["authorities"][0]["user"];

export default tokenToUser;
