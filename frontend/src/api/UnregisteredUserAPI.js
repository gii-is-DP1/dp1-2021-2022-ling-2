import axios from "axios";
import { API_BASE_URL } from "../constants/paths";

// const HEADERS = {
//   "Access-Control-Allow-Origin": "*",
//   "Access-Control-Allow-Credentials": "true",
//   "Access-Control-Allow-Methods":
//     "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL",
//   "Access-Control-Allow-Headers":
//     "Overwrite, Destination, Content-Type, Depth, User-Agent, Translate, Range, Content-Range, Timeout, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control, Location, Lock-Token, If",
//   "Access-Control-Expose-Headers": "DAV, content-length, Allow",
// };

const UnregisteredUserAPI = {
  getCredentials: function () {
    return new Promise(function (resolve, reject) {
      axios
        .get(`http://localhost:8080/api/unregistered-users`, {})
        .then((response) => resolve(response))
        .catch((error) => reject(console.log(error)));
    });
  },
};

export { UnregisteredUserAPI };
