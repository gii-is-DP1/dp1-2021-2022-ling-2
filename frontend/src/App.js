import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.css";
import * as ROUTES from "./constants/routes";
import Home from "./pages/home";
import Login from "./pages/login";
import NotFound from "./pages/not-found";
import SignUp from "./pages/signup";

export default function App() {
  return (
    <Router>
      <Switch>
        <Route exact path={ROUTES.LOGIN} component={Login} />
        <Route exact path={ROUTES.SIGNUP} component={SignUp} />
        <Route exact path={ROUTES.HOME} component={Home} />
        <Route component={NotFound} />
        {/* All routes have "exact" since that means that the path has to match the exact string. If a route is not correct, the fallback is ROUTES.NOT_FOUND since it's the only one without "exact" */}
      </Switch>
    </Router>
  );
}
