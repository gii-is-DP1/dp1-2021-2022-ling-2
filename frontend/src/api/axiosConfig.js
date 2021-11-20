import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/api";
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";

const token = localStorage.getItem("token");
const headers = token ? { Authorization: `Bearer ${token}` } : null;

const instance = axios.create({
  headers: headers,
});

export default instance;
