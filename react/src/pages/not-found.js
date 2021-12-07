import { useEffect } from "react";
import HomeButton from "../components/common/home-button";

/**
 *
 * @author andrsdt
 */
export default function NotFound() {
  useEffect(() => {
    document.title = "Not Found for Heroes";
  }, []);

  return (
    <>
      <HomeButton />
      <div className="flex h-screen bg-wood p-8 justify-center items-center text-center">
        <div className="flex flex-col space-y-4 btn-ntfh p-6 rounded-3xl">
          <p className="text-gradient-ntfh text-6xl">Not found</p>
          <p className="text-6xl">🙁</p>
        </div>
      </div>
    </>
  );
}
