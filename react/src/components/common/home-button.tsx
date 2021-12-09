import * as ROUTES from "../../constants/routes";
import { Link } from "react-router-dom";

export default function HomeButton() {
  return (
    <div className="fixed p-8">
      <Link to={ROUTES.HOME}>
        <button className="btn-ntfh">
          <p className="text-2xl text-gradient-ntfh">Home</p>
        </button>
      </Link>
    </div>
  );
}
