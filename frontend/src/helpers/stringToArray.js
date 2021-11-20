/**
 * @author andrsdt
 * @param {String} string a string of the form "[a,b,c]"
 * @return {Array} array of the form ["a","b","c"]
 */
const stringToArray = (string) => {
  return string.substring(1, string.length - 1).split(",");
};

export default stringToArray;
