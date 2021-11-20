import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/api";
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";

const instance = axios.create({
  // This was leading to some problems
  // headers: headers,
});

export default instance;
