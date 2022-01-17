import moment from "moment";

const timeParser = (unix_timestamp: number | undefined): string =>
  moment(unix_timestamp).format("YYYY-MM-DD hh:mm:ss");
export default timeParser;
