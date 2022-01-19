import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import AchievementsTable from "../components/admin/AchievementsTable";
import HomeButton from "../components/common/home-button";
import { Achievement } from "../interfaces/Achievement";
import * as ROUTES from "../constants/routes";

/**
 *
 * @author andrsdt
 */
export default function UserAchievements() {
  const { username } = useParams<{ username: string }>();
  const [achievements, setAchievements] = useState<Achievement[]>([]);

  useEffect(() => {
    const fetchUserAchievements = async () => {
      try {
        const response = await axios.get(`/users/${username}/achievements`);
        setAchievements(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    };

    fetchUserAchievements();
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">
              {username}'s achievements
            </p>
          </button>
        </span>
        <div className="w-1/2">
          <AchievementsTable achievements={achievements} />
          <button className="btn-ntfh">
            <Link to={ROUTES.ALL_ACHIEVEMENTS} className="flex items-center">
              <p className="text-gradient-ntfh">All achievements</p>
            </Link>
          </button>
        </div>
      </div>
    </>
  );
}
