import { Link } from "react-router-dom";
import * as ROUTES from "../../constants/routes";

export default function Homebar() {
  // TODO do we even have a homebar?
  // TODO move to "common" components since this appears in multiple places
  return (
    <div>
      <Link to={ROUTES.HOME}>
        <h1>Home</h1>
      </Link>
    </div>
  );
}
