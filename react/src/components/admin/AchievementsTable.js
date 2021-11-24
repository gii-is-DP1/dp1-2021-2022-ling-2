import errorContext from "../../context/error";
import { useContext, useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import axios from "../../api/axiosConfig";

export default function AchievementsTable() {
  const { errors, setErrors } = useContext(errorContext);
  const [achievements, setAchievements] = useState([]);

  return "Achievements Table";
}
