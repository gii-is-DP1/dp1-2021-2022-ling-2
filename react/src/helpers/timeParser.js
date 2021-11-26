import moment from "moment";

const timeParser = (unix_timestamp) =>
  moment(unix_timestamp).format("YYYY-MM-DD hh:mm:ss");
export default timeParser;
