import axios from "axios";
import { API_BASE_URL } from "../constants/paths";

axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";
// axios.defaults.headers.common["Access-Control-Allow-Credentials"] = "true";
axios.defaults.xsrfHeaderName = "X-XSRF-TOKEN";
axios.defaults.withCredentials = true;
const instance = axios.create();

// ! It works if we pass the token as "X-XSRF-TOKEN" as a header (tested with Postman)
// instance.interceptors.request.use((config) => {
//   config && config.headers && config.headers.common["X-XSRF-TOKEN"];
//   return config;
// });

instance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    return Promise.reject({
      message: error.response.data.message,
      status: error.response.status,
    });
  }
);

export default instance;
