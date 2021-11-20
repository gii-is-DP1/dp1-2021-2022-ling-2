import axios from "axios";
import { API_BASE_URL } from "../constants/paths";

axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";

const instance = axios.create();

export default instance;
