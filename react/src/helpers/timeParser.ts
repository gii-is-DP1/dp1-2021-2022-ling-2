import moment from "moment";

const timeParser = (unix_timestamp: number): string =>
  moment(unix_timestamp).format("YYYY-MM-DD hh:mm:ss");
export default timeParser;
