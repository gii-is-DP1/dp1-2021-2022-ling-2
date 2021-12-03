import { Link } from "react-router-dom";
import * as ROUTES from "../constants/routes";

/**
 *
 * @author andrsdt
 */
export default function NotFound() {
  return (
    <div>
      <h1>Not found 🙁</h1>
      <br />
      <Link to={ROUTES.HOME}>Return Home</Link>
    </div>
  );
}
