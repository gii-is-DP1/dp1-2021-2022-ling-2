import AchievementsTable from "../components/admin/AchievementsTable";
import HomeButton from "../components/common/home-button";

/**
 *
 * @author andrsdt
 */
export default function Achievements() {
  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8">
        <span className="text-center pb-8">
          <button type="submit" className="btn-ntfh">
            <p className="text-5xl text-gradient-ntfh">Achievements</p>
          </button>
        </span>
        <AchievementsTable />
      </div>
    </>
  );
}
