import { useState, useEffect } from "react";
import axios from "../api/axiosConfig";
import toast from "react-hot-toast";
import HomeButton from "../components/common/home-button";

/**
 *
 * @author andrsdt
 */
export default function Statistics() {
  const [gamesHistory, setGamesHistory] = useState(null);

  useEffect(() => {
    const fetchGameHistoryCount = async () => {
      try {
        const response = await axios.get(`gameHistory`);
        setGamesHistory(response.data);
      } catch (error) {
        toast.error(error.response?.data?.message);
      }
    };

    fetchGameHistoryCount();
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Statistics</p>
          </button>
        </span>
        <div className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-8 text-2xl">
          <h2>Total Games Played: {gamesHistory && gamesHistory.length}</h2>
        </div>
      </div>
    </>
  );
}