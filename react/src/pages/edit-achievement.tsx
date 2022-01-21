import React, { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import userContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";
import { Achievement } from "../interfaces/Achievement";

/**
 *
 * @author andrsdt
 */
export default function EditAchievement() {
  const params: any = useParams();
  const history = useHistory();
  const { userToken } = useContext(userContext); // hook
  const loggedUser = tokenParser(useContext(userContext)); // hook

  const [achievement, setAchievement] = useState<Achievement | undefined>(
    undefined
  );
  const [name, setName] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [condition, setCondition] = useState<number | undefined>(undefined);
  const [achievementType, setAchievementType] = useState<string>("");
  const [achievementTypeOptions, setAchievementTypeOptions] = useState<
    string[]
  >([]);
  const sendToAdminPage = () => history.push(ROUTES.ADMIN_PAGE);

  async function fetchAchievement() {
    try {
      const response = await axios.get(`/achievements/${params.achievementId}`);
      setAchievement(response.data);
      setName(response.data.name);
      setDescription(response.data.description);
      setCondition(response.data.condition);
      setAchievementType(response.data.type);
    } catch (error: any) {
      toast.error(error?.message);
      sendToAdminPage();
    }
  }

  async function handleSubmit(event: React.MouseEvent) {
    event.preventDefault();
    try {
      const payload = {
        id: achievement?.id, // Needed to identify the achievement
        version: achievement?.version,
        name: name,
        description: description,
        condition: condition ?? 0,
        type: achievementType,
      };
      await axios.put("/achievements", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      toast.success("Achievement edited successfully");
      sendToAdminPage();
    } catch (error: any) {
      toast.error(error?.message);
    }
  }

  useEffect(() => {
    async function fetchAchievementTypeOptions() {
      try {
        const response = await axios.get("/achievements/types", {
          headers: { Authorization: "Bearer " + userToken },
        });
        setAchievementTypeOptions(response.data);
      } catch (error: any) {
        toast.error(error?.message);
      }
    }

    document.title = `NTFH - Edit achievement`;
    if (!hasAuthority(loggedUser, "admin")) {
      toast.error("You must be an admin to access this page");
      history.replace(ROUTES.LOGIN);
    }
    fetchAchievement();
    fetchAchievementTypeOptions();
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Edit achievement</p>
          </button>
        </span>
        <form className="flex flex-col bg-felt rounded-3xl border-20 border-gray-900 p-8 w-1/2">
          <div className="flex flex-col mb-4">
            <p className="font-bold text-2xl mb-2">Name</p>
            <input
              value={name}
              type="text"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) => setName(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-4">
            <p className="font-bold text-2xl mb-2">Description</p>
            <input
              value={description}
              type="text"
              className="p-3 rounded-xl border-4 border-black text-black break-all"
              onChange={(e) => setDescription(e.target.value)}
            ></input>
          </div>
          <div className="flex flex-col mb-4">
            <p className="font-bold text-2xl mb-2">Condition {"{X}"}</p>
            <input
              value={condition}
              type="text"
              className="p-3 rounded-xl border-4 border-black text-black"
              onChange={(e) =>
                setCondition(parseInt(e.target.value) || undefined)
              }
            ></input>
          </div>
          <div className="flex mb-4">
            <p className="font-bold text-2xl mb-2 mr-2">Type</p>
            <select
              name="achievement-type"
              value={achievementType}
              onChange={(e) => setAchievementType(e.target.value)}
            >
              {achievementTypeOptions.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>
          <button type="submit" className="btn-ntfh" onClick={handleSubmit}>
            <p className="text-gradient-ntfh text-5xl p-2">Edit</p>
          </button>
        </form>
      </div>
    </>
  );
}
