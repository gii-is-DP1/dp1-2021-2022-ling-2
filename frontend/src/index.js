import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import "./index.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";

ReactDOM.render(
  // Start point of our application
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById("root")
);
