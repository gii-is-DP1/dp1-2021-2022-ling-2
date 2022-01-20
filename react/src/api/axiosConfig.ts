import axios from "axios";
import { API_BASE_URL } from "../constants/paths";

axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";
axios.defaults.withCredentials = true;

const instance = axios.create();

instance.interceptors.response.use(
  (response) => response,
  (error) =>
    Promise.reject({
      message: error.response.data.message,
      status: error.response.status,
    })
);

export default instance;
