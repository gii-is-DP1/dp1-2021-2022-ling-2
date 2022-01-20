import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";

/**
 *
 * @author andrsdt
 */
export default function Statistics() {
  const [gamesHistoryCount, setGamesHistoryCount] = useState(0);

  useEffect(() => {
    const fetchGameHistoryCount = async () => {
      try {
        const response = await axios.get(`statistics/games/count`);
        setGamesHistoryCount(response.data);
      } catch (error: any) {
        toast.error(error?.message);
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
            <p className="text-5xl text-gradient-ntfh">Global statistics</p>
          </button>
        </span>
        <div className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-8 text-2xl">
          <h2>Total Games Played: {gamesHistoryCount}</h2>
        </div>
      </div>
    </>
  );
}
