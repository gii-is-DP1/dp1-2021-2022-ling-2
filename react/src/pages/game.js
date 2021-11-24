import axios from "../api/axiosConfig";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function Game() {
  // route for /games/gameId
  const { gameId } = useParams(); // get params from react router link
}
