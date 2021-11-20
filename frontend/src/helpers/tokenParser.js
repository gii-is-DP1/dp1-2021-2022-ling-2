import jwtDecode from "jwt-decode";

/**
 * @author andrsdt
 * @param {String} jwt Token returned by the server
 * @return Object with email, id and
 */

/**
 * @author andrsdt
 * @param {String} string a string of the form "[a,b,c]"
 * @return {Array} array of the form ["a","b","c"]
 */
const stringToArray = (string) => {
  return string.substring(1, string.length - 1).split(",");
};

const tokenParser = (context) => {
  const data = jwtDecode(context.userToken)["data"];
  data.authorities = stringToArray(data.authorities); // This comes as a string. TODO change in the backend. This workaround works meanwhile
  return data;
};

export default tokenParser;
