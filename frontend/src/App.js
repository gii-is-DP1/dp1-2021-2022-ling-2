import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.css";
import * as ROUTES from "./constants/routes";
import UserContext from "./context/user";
import { useLocalStorage } from "./hooks/useLocalStorage";
import Home from "./pages/home";
import Login from "./pages/login";
import NotFound from "./pages/not-found";
import SignUp from "./pages/signup";
import tokenToUser from "./helpers/tokenToUser";

export default function App() {
  const [token, setToken] = useLocalStorage("token", null);

  return (
    <UserContext.Provider value={token ? tokenToUser(token) : null}>
      <Router>
        <Switch>
          <Route exact path={ROUTES.LOGIN} component={Login} />
          <Route exact path={ROUTES.SIGNUP} component={SignUp} />
          <Route exact path={ROUTES.HOME} component={Home} />
          <Route component={NotFound} />
          {/* All routes have "exact" since that means that the path has to match the exact string. If a route is not correct, the fallback is ROUTES.NOT_FOUND since it's the only one without "exact" */}
        </Switch>
      </Router>
    </UserContext.Provider>
  );
}
