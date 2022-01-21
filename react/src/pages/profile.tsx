import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import GamesHistoryTable from "../components/admin/GamesHistoryTable";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import { User } from "../interfaces/User";

/**
 *
 * @author andrsdt
 */
export default function Profile() {
  const history = useHistory(); // hook
  const { username: profileUsername } = useParams<{ username: string }>(); // hook
  const [userProfile, setUserProfile] = useState<User | null>(null);

  useEffect(() => {
    document.title = `NTFH - ${profileUsername}'s profile`;

    // get user profile
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(`users/${profileUsername}`);
        setUserProfile(response.data);
      } catch (error: any) {
        toast.error(error?.message);
        history.push("/not-found");
      }
    };
    fetchUserProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty array means "run only first time the component renders"

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8">
        <span className="text-center pb-8">
          <Link to={ROUTES.PROFILE.replace(":username", profileUsername)}>
            <button type="submit" className="btn-ntfh">
              <p className="text-5xl text-gradient-ntfh">Profile</p>
            </button>
          </Link>
        </span>
        <span className="flex flex-row justify-around ">
          <div className="flex flex-col max-w-1/3 space-y-4 text-2xl bg-felt border-20 border-gray-900 rounded-3xl m-8 shadow-2xl p-6">
            <div className="flex flex-col -space-y-2">
              <span className="font-semibold">Username</span>
              <span>{userProfile?.username}</span>
            </div>
            <div className="flex flex-col -space-y-2 pt-6 ">
              <span className="font-semibold">Email</span>
              <span>{userProfile?.email}</span>
            </div>
            <div className="flex flex-col space-y-3">
              <Link
                to={ROUTES.USER_ACHIEVEMENTS.replace(
                  ":username",
                  profileUsername
                )}
              >
                <button type="submit" className="btn-ntfh">
                  <p className="text-2xl text-gradient-ntfh">Achievements</p>
                </button>
              </Link>
              <span className="flex space-x-2">
                <Link
                  to={ROUTES.USER_STATISTICS.replace(
                    ":username",
                    profileUsername
                  )}
                >
                  <button type="submit" className="btn-ntfh">
                    <p className="text-2xl text-gradient-ntfh">Stats</p>
                  </button>
                </Link>
                <Link
                  to={ROUTES.EDIT_PROFILE.replace(":username", profileUsername)}
                >
                  <button type="submit" className="btn-ntfh">
                    <p className="text-2xl text-gradient-ntfh">Edit</p>
                  </button>
                </Link>
              </span>
            </div>
          </div>
          <div className="flex flex-col w-3/4">
            {/* match history table */}
            <GamesHistoryTable />
          </div>
        </span>
      </div>
    </>
  );
}
