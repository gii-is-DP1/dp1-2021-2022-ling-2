import axios from "axios";
import { API_BASE_URL } from "../constants/paths";

axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";
// axios.defaults.headers.common["Access-Control-Allow-Credentials"] = "true";
axios.defaults.withCredentials = true;

const instance = axios.create();

instance.interceptors.request.use((config) => {
  config.xsrfHeaderName = "X-CSRF-TOKEN";
  return config;
});

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
